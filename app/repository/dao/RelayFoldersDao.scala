package repository.dao

import java.sql.ResultSet

import domain.`object`.folder.RelayFolders.RelayFoldersDto

import scala.util.Try

class RelayFoldersDao {

  private val tableName = "relay_folders"

  private val relayFoldersDto = (rs: ResultSet) => {
    val folderId = rs.getInt("folder_id")
    val parentFolderId = rs.getInt("parent_folder_id")
    RelayFoldersDto(folderId, parentFolderId)
  }

  def selectAllBy(parentFolderIds: Seq[Int]): Try[Seq[RelayFoldersDto]] = {
    val sql = s"select * from $tableName where parent_folder_id in (${parentFolderIds.mkString(",")})"
    DBAccessor.selectRecords(sql, relayFoldersDto)
  }

  def insert(folderId: Int, parentFolderId: Int): Try[Int] = {
    val sql = s"insert into $tableName (folder_id, parent_folder_id) values ($folderId, $parentFolderId)"
    DBAccessor.execute(sql)
  }

  def deleteBy(folderIds: Seq[Int]): Try[Int] = {
    val sql = s"delete from $tableName where folder_id in (${folderIds.mkString(",")})"
    DBAccessor.execute(sql)
  }
}
