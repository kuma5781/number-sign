package repository

import domain.`object`.folder.{ FolderId, RelayFolders }
import repository.dao.RelayFoldersDao

import scala.util.Try

class RelayFoldersRepository(relayFoldersDao: RelayFoldersDao = new RelayFoldersDao) {

  def findAllBy(parentFolderIds: Seq[FolderId]): Try[Seq[RelayFolders]] = {
    relayFoldersDao.selectAllBy(parentFolderIds.map(_.value)).map { dtos =>
      dtos.map(RelayFolders(_))
    }
  }

  def save(folderId: FolderId, parentFolderId: FolderId): Try[Int] =
    relayFoldersDao.insert(folderId.value, parentFolderId.value)

  def removeBy(folderIds: Seq[FolderId]): Try[Int] =
    relayFoldersDao.deleteBy(folderIds.map(_.value))
}
