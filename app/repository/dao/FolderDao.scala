package repository.dao

import java.sql.ResultSet

import domain.`object`.folder.Folder.FolderDto
import domain.`object`.folder.NewFolder.NewFolderDto

import scala.util.Try

class FolderDao {

  private val tableName = "folder"
  private val relayFoldersTableName = "relay_folders"

  private val folderDto = (rs: ResultSet) => {
    val id = rs.getInt("id")
    val userId = rs.getInt("user_id")
    val name = rs.getString("name")
    val parentFolderId = rs.getInt("parent_folder_id") match {
      case 0 => None
      case num => Some(num)
    }
    FolderDto(id, userId, name, parentFolderId)
  }

  def selectAllByUserId(userId: Int): Try[Seq[FolderDto]] = {
    val sql =
      s"""
         |select f.id, user_id, name, parent_folder_id
         |from $tableName as f
         |left join $relayFoldersTableName as rf
         |on f.id = rf.folder_id
         |where user_id = $userId
			""".stripMargin
    DBAccessor.selectRecords(sql, folderDto)
  }

  def insertAndGetId(newFolderDto: NewFolderDto): Try[Int] = {
    val sql = s"insert into $tableName (user_id, name) values (${newFolderDto.userId}, '${newFolderDto.name}')"
    DBAccessor.executeAndGetId(sql)
  }

  def updateName(folderId: Int, name: String): Try[Int] = {
    val sql = s"update $tableName set name = '$name' where id = $folderId"
    DBAccessor.execute(sql)
  }

  def deleteBy(folderIds: Seq[Int]): Try[Int] = {
    val sql = s"delete from $tableName where id in (${folderIds.mkString(",")})"
    DBAccessor.execute(sql)
  }
}
