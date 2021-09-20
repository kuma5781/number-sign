package repository.dao

import java.sql.ResultSet

import domain.`object`.note.RelayNoteFolder.RelayNoteFolderDto
import org.scalatestplus.play.PlaySpec
import testSupport.DBSupport

class RelayNoteFolderDaoSpec extends PlaySpec {

  trait Context {
    val relayNoteFolderDao = new RelayNoteFolderDao
    val tableName = "relay_note_folder"

    val noteId = 1
    val parentFolderId = 1

    val relayNoteFolderDto = (rs: ResultSet) => {
      val noteId = rs.getInt("note_id")
      val parentFolderId = rs.getInt("parent_folder_id")
      RelayNoteFolderDto(noteId, parentFolderId)
    }
    val selectAllSql = s"select * from $tableName"
  }

  "#insert" should {
    "insert relay_note_folder record" in new Context {
      DBSupport.dbTest(
        tableName, {
          relayNoteFolderDao.insert(noteId, parentFolderId)

          val relayFoldersDtos = DBAccessor.selectRecords(selectAllSql, relayNoteFolderDto).get

          relayFoldersDtos(0).noteId mustBe noteId
          relayFoldersDtos(0).parentFolderId mustBe parentFolderId
        }
      )
    }
  }
}
