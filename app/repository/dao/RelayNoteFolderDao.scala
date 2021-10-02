package repository.dao

import java.sql.ResultSet

import domain.`object`.note.RelayNoteFolder.RelayNoteFolderDto

import scala.util.Try

class RelayNoteFolderDao {

  private val tableName = "relay_note_folder"

  private val relayNoteFolderDao = (rs: ResultSet) => {
    val noteId = rs.getInt("note_id")
    val parentFolderId = rs.getInt("parent_folder_id")
    RelayNoteFolderDto(noteId, parentFolderId)
  }

  def selectAllBy(parentFolderIds: Seq[Int]): Try[Seq[RelayNoteFolderDto]] = {
    val sql = s"select * from $tableName where parent_folder_id in (${parentFolderIds.mkString(",")})"
    DBAccessor.selectRecords(sql, relayNoteFolderDao)
  }

  def insert(noteId: Int, parentFolderId: Int): Try[Int] = {
    val sql = s"insert into $tableName (note_id, parent_folder_id) values ($noteId, '$parentFolderId')"
    DBAccessor.execute(sql)
  }

  def deleteBy(noteIds: Seq[Int]): Try[Int] = {
    val sql = s"delete from $tableName where note_id in (${noteIds.mkString(",")})"
    DBAccessor.execute(sql)
  }
}
