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

    val userId = 1
    val name = "name"
    val newFolderDto = NewFolderDto(userId, name, None)

    val folderDto = (rs: ResultSet) => {
      val id = rs.getInt("id")
      val userId = rs.getInt("user_id")
      val name = rs.getString("name")
      FolderDto(id, userId, name)
    }

    val selectAllSql = s"select * from $tableName"
  }

  "#insertAndGetId" should {
    "insert folder record and return last inserted id" in new Context {
      DBSupport.dbTest(
        tableName, {
          val folderId = folderDao.insertAndGetId(newFolderDto).get

          val folderDtos = DBAccessor.selectRecords(selectAllSql, folderDto).get

          folderDtos(0).id mustBe folderId
          folderDtos(0).userId mustBe userId
          folderDtos(0).name mustBe name
        }
      )
    }
  }
}
