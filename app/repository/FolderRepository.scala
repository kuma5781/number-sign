package repository

import domain.`object`.folder.{ FolderId, NewFolder }
import domain.`object`.folder.NewFolder.NewFolderDto
import repository.dao.FolderDao

import scala.util.Try

class FolderRepository(folderDao: FolderDao = new FolderDao) {

  def saveAndGetFolderId(newFolder: NewFolder): Try[FolderId] = {
    val newFolderDto = NewFolderDto(
      newFolder.userId.value,
      newFolder.name.value,
      None
    )
    folderDao.insertAndGetId(newFolderDto).map(FolderId)
  }
}
