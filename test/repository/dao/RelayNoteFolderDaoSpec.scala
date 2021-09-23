package repository.dao

import java.sql.ResultSet

import domain.`object`.note.RelayNoteFolder.RelayNoteFolderDto
import org.scalatestplus.play.PlaySpec
import testSupport.DBSupport

class RelayNoteFolderDaoSpec extends PlaySpec {

  trait Context {
    val relayNoteFolderDao = new RelayNoteFolderDao
    val tableName = "relay_note_folder"

    val noteId1 = 1
    val noteId2 = 2
    val parentFolderId1 = 1
    val parentFolderId2 = 2

    val parentFolderIds = Seq(parentFolderId1, parentFolderId2)

    val relayNoteFolderDto = (rs: ResultSet) => {
      val noteId = rs.getInt("note_id")
      val parentFolderId = rs.getInt("parent_folder_id")
      RelayNoteFolderDto(noteId, parentFolderId)
    }
    val selectAllSql = s"select * from $tableName"
    val insertSql = (noteId: Int, parentFolderId: Int) =>
      s"insert into $tableName (note_id, parent_folder_id) values ($noteId, '$parentFolderId')"
  }

  "#selectAllBy" should {
    "return all relay_note_folder records associated with parentFolderIds" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(noteId1, parentFolderId1))
          DBAccessor.execute(insertSql(noteId2, parentFolderId2))

          val relayNoteFolderDtos = relayNoteFolderDao.selectAllBy(parentFolderIds).get

          relayNoteFolderDtos.length mustBe 2
          relayNoteFolderDtos(0).noteId mustBe noteId1
          relayNoteFolderDtos(0).parentFolderId mustBe parentFolderId1
          relayNoteFolderDtos(1).noteId mustBe noteId2
          relayNoteFolderDtos(1).parentFolderId mustBe parentFolderId2
        }
      )
    }
  }

  "#insert" should {
    "insert relay_note_folder record" in new Context {
      DBSupport.dbTest(
        tableName, {
          relayNoteFolderDao.insert(noteId1, parentFolderId1)

          val relayFoldersDtos = DBAccessor.selectRecords(selectAllSql, relayNoteFolderDto).get

          relayFoldersDtos(0).noteId mustBe noteId1
          relayFoldersDtos(0).parentFolderId mustBe parentFolderId1
        }
      )
    }
  }

  "#deleteBy" should {
    "delete relay_note_folder records associated with parentFolderIds" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(noteId1, parentFolderId1))
          DBAccessor.execute(insertSql(noteId2, parentFolderId2))

          val beforeRelayNoteFolderDtos = DBAccessor.selectRecords(selectAllSql, relayNoteFolderDto).get

          beforeRelayNoteFolderDtos.length mustBe 2

          val noteIds = beforeRelayNoteFolderDtos.map(_.noteId)

          relayNoteFolderDao.deleteBy(noteIds)

          val deletedRelayNoteFolderDtos = DBAccessor.selectRecords(selectAllSql, relayNoteFolderDto).get

          deletedRelayNoteFolderDtos.length mustBe 0
        }
      )
    }
  }
}
