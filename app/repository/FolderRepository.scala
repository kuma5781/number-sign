package repository

import domain.`object`.folder.{ Folder, FolderId, FolderName, NewFolder }
import domain.`object`.folder.NewFolder.NewFolderDto
import domain.`object`.user.UserId
import repository.dao.FolderDao

import scala.util.Try

class FolderRepository(folderDao: FolderDao = new FolderDao) {

  def findAllBy(userId: UserId): Try[Seq[Folder]] =
    folderDao.selectAllByUserId(userId.value).map { dtos =>
      dtos.map(Folder(_))
    }

  def saveAndGetFolderId(newFolder: NewFolder): Try[FolderId] = {
    val newFolderDto = NewFolderDto(
      newFolder.userId.value,
      newFolder.name.value,
      None
    )
    folderDao.insertAndGetId(newFolderDto).map(FolderId)
  }

  def updateName(folderId: FolderId, name: FolderName): Try[Int] =
    folderDao.updateName(folderId.value, name.value)

  def removeBy(folderIds: Seq[FolderId]): Try[Int] =
    folderDao.deleteBy(folderIds.map(_.value))
}
