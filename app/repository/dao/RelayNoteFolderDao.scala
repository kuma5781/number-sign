package repository.dao

import scala.util.Try

class RelayNoteFolderDao {

  private val tableName = "relay_note_folder"

  def insert(noteId: Int, parentFolderId: Int): Try[Int] = {
    val sql = s"insert into $tableName (note_id, parent_folder_id) values ($noteId, '$parentFolderId')"
    DBAccessor.execute(sql)
  }
}
