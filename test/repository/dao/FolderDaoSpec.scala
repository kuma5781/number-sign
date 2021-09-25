package repository.dao

import java.sql.ResultSet

import domain.`object`.folder.Folder.FolderDto
import domain.`object`.folder.NewFolder.NewFolderDto
import org.scalatestplus.play.PlaySpec
import testSupport.DBSupport

class FolderDaoSpec extends PlaySpec {

  trait Context {
    val folderDao = new FolderDao
    val tableName = "folder"

    val userId1 = 1
    val name1 = "name1"
    val newFolderDto1 = NewFolderDto(userId1, name1, None)

    val userId2 = 2
    val name2 = "name2"
    val newFolderDto2 = NewFolderDto(userId2, name2, None)

    val folderDto = (rs: ResultSet) => {
      val id = rs.getInt("id")
      val userId = rs.getInt("user_id")
      val name = rs.getString("name")
      FolderDto(id, userId, name)
    }

    val selectAllSql = s"select * from $tableName"
    val insertSql = (newFolderDto: NewFolderDto) =>
      s"insert into $tableName (user_id, name) values ('${newFolderDto.userId}', '${newFolderDto.name}')"
  }

  "#insertAndGetId" should {
    "insert folder record and return last inserted id" in new Context {
      DBSupport.dbTest(
        tableName, {
          val folderId = folderDao.insertAndGetId(newFolderDto1).get

          val folderDtos = DBAccessor.selectRecords(selectAllSql, folderDto).get

          folderDtos(0).id mustBe folderId
          folderDtos(0).userId mustBe userId1
          folderDtos(0).name mustBe name1
        }
      )
    }
  }

  "#updateName" should {
    "update folder name associated with folderIds" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newFolderDto1))
          val beforeFolderDtos = DBAccessor.selectRecords(selectAllSql, folderDto).get

          beforeFolderDtos(0).name mustBe name1
          val folderId = beforeFolderDtos(0).id

          folderDao.updateName(folderId, name2)

          val updatedFolderDtos = DBAccessor.selectRecords(selectAllSql, folderDto).get

          updatedFolderDtos(0).name mustBe name2
        }
      )
    }
  }

  "#deleteBy" should {
    "delete folder records associated with folderIds" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newFolderDto1))
          DBAccessor.execute(insertSql(newFolderDto2))
          val beforeFolderDtos = DBAccessor.selectRecords(selectAllSql, folderDto).get

          beforeFolderDtos.length mustBe 2

          val folderIds = beforeFolderDtos.map(_.id)

          folderDao.deleteBy(folderIds)

          val deletedFolderDtos = DBAccessor.selectRecords(selectAllSql, folderDto).get

          deletedFolderDtos.length mustBe 0
        }
      )
    }
  }
}
