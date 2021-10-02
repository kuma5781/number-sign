package repository.dao

import domain.`object`.folder.NewFolder.NewFolderDto

import scala.util.Try

class FolderDao {

  private val tableName = "folder"

  def insertAndGetId(newFolderDto: NewFolderDto): Try[Int] = {
    val sql = s"insert into $tableName (user_id, name) values ('${newFolderDto.userId}', '${newFolderDto.name}')"
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
