package repository.dao

import java.sql.ResultSet

import domain.`object`.folder.RelayFolders.RelayFoldersDto
import org.scalatestplus.play.PlaySpec
import testSupport.DBSupport

class RelayFoldersDaoSpec extends PlaySpec {

  trait Context {
    val relayFoldersDao = new RelayFoldersDao
    val tableName = "relay_folders"

    val folderId = 1
    val parentFolderId = 2

    val relayFoldersDto = (rs: ResultSet) => {
      val folderId = rs.getInt("folder_id")
      val parentFolderId = rs.getInt("parent_folder_id")
      RelayFoldersDto(folderId, parentFolderId)
    }
    val selectAllSql = s"select * from $tableName"
  }

  "#insert" should {
    "insert relay_folders record" in new Context {
      DBSupport.dbTest(
        tableName, {
          relayFoldersDao.insert(folderId, parentFolderId)

          val relayFoldersDtos = DBAccessor.selectRecords(selectAllSql, relayFoldersDto).get

          relayFoldersDtos(0).folderId mustBe folderId
          relayFoldersDtos(0).parentFolderId mustBe parentFolderId
        }
      )
    }
  }
}
