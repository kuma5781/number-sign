package repository.dao

import java.sql.ResultSet

import domain.`object`.folder.RelayFolders.RelayFoldersDto
import org.scalatestplus.play.PlaySpec
import testSupport.DBSupport

class RelayFoldersDaoSpec extends PlaySpec {

  trait Context {
    val relayFoldersDao = new RelayFoldersDao
    val tableName = "relay_folders"

    val folderId1 = 1
    val folderId2 = 2
    val parentFolderId1 = 2
    val parentFolderId2 = 3

    val parentFolderIds = Seq(parentFolderId1, parentFolderId2)

    val relayFoldersDto = (rs: ResultSet) => {
      val folderId = rs.getInt("folder_id")
      val parentFolderId = rs.getInt("parent_folder_id")
      RelayFoldersDto(folderId, parentFolderId)
    }
    val selectAllSql = s"select * from $tableName"
    val insertSql = (folderId: Int, parentFolderId: Int) =>
      s"insert into $tableName (folder_id, parent_folder_id) values ($folderId, $parentFolderId)"
  }

  "#selectAllBy" should {
    "return all relay_folders records associated with parentFolderIds" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(folderId1, parentFolderId1))
          DBAccessor.execute(insertSql(folderId2, parentFolderId2))

          val relayFoldersDtos = relayFoldersDao.selectAllBy(parentFolderIds).get

          relayFoldersDtos.length mustBe 2
          relayFoldersDtos(0).folderId mustBe folderId1
          relayFoldersDtos(0).parentFolderId mustBe parentFolderId1
          relayFoldersDtos(1).folderId mustBe folderId2
          relayFoldersDtos(1).parentFolderId mustBe parentFolderId2
        }
      )
    }
  }

  "#insert" should {
    "insert relay_folders record" in new Context {
      DBSupport.dbTest(
        tableName, {
          relayFoldersDao.insert(folderId1, parentFolderId1)

          val relayFoldersDtos = DBAccessor.selectRecords(selectAllSql, relayFoldersDto).get

          relayFoldersDtos(0).folderId mustBe folderId1
          relayFoldersDtos(0).parentFolderId mustBe parentFolderId1
        }
      )
    }
  }

  "#deleteBy" should {
    "delete relay_folders records associated with parentFolderIds" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(folderId1, parentFolderId1))
          DBAccessor.execute(insertSql(folderId2, parentFolderId2))

          val beforeRelayFoldersDtos = DBAccessor.selectRecords(selectAllSql, relayFoldersDto).get

          beforeRelayFoldersDtos.length mustBe 2

          val folderIds = beforeRelayFoldersDtos.map(_.folderId)

          relayFoldersDao.deleteBy(folderIds)

          val deletedRelayFoldersDtos = DBAccessor.selectRecords(selectAllSql, relayFoldersDto).get

          deletedRelayFoldersDtos.length mustBe 0
        }
      )
    }
  }
}
