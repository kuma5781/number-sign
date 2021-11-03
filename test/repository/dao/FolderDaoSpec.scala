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
    val relayFoldersTableName = "relay_folders"

    val userId = 1
    val name1 = "name1"
    val parentFolderId1 = 1
    val newFolderDto1 = NewFolderDto(userId, name1, None)

    val name2 = "name2"
    val newFolderDto2 = NewFolderDto(userId, name2, None)

    val folderDto = (rs: ResultSet) => {
      val id = rs.getInt("id")
      val userId = rs.getInt("user_id")
      val name = rs.getString("name")
      FolderDto(id, userId, name, None)
    }

    val selectAllSql = s"select * from $tableName"
    val insertFolderSql = (newFolderDto: NewFolderDto) =>
      s"insert into $tableName (user_id, name) values (${newFolderDto.userId}, '${newFolderDto.name}')"
    val insertRelayFoldersSql = (folderId: Int, parentFolderId: Int) =>
      s"insert into $relayFoldersTableName (folder_id, parent_folder_id) values ($folderId, $parentFolderId)"
  }

  "#selectAllByUserId" should {
    "return all folder record associated with userId" in new Context {
      DBSupport.dbTest(
        Seq(tableName, relayFoldersTableName), {
          DBAccessor.execute(insertFolderSql(newFolderDto1))
          DBAccessor.execute(insertFolderSql(newFolderDto2))

          val folderDtos = DBAccessor.selectRecords(selectAllSql, folderDto).get
          val folderId = folderDtos(0).id

          DBAccessor.execute(insertRelayFoldersSql(folderId, parentFolderId1))

          val selectFolderDtos = folderDao.selectAllByUserId(userId).get

          selectFolderDtos.length mustBe 2
          selectFolderDtos(0).userId mustBe userId
          selectFolderDtos(0).name mustBe name1
          selectFolderDtos(0).parentFolderId mustBe Some(parentFolderId1)
          selectFolderDtos(1).userId mustBe userId
          selectFolderDtos(1).name mustBe name2
          selectFolderDtos(1).parentFolderId mustBe None
        }
      )
    }
  }

  "#insertAndGetId" should {
    "insert folder record and return last inserted id" in new Context {
      DBSupport.dbTest(
        tableName, {
          val folderId = folderDao.insertAndGetId(newFolderDto1).get

          val folderDtos = DBAccessor.selectRecords(selectAllSql, folderDto).get

          folderDtos(0).id mustBe folderId
          folderDtos(0).userId mustBe userId
          folderDtos(0).name mustBe name1
        }
      )
    }
  }

  "#updateName" should {
    "update folder name associated with folderIds" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertFolderSql(newFolderDto1))
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
          DBAccessor.execute(insertFolderSql(newFolderDto1))
          DBAccessor.execute(insertFolderSql(newFolderDto2))
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
