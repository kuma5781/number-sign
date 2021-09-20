package repository.dao

import scala.util.Try

class RelayFoldersDao {

  private val tableName = "relay_folders"

  def insert(folderId: Int, parentFolderId: Int): Try[Int] = {
    val sql = s"insert into $tableName (folder_id, parent_folder_id) values ($folderId, $parentFolderId)"
    DBAccessor.execute(sql)
  }
}
