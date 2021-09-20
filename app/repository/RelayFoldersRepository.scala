package repository

import domain.`object`.folder.FolderId
import repository.dao.RelayFoldersDao

import scala.util.Try

class RelayFoldersRepository(relayFoldersDao: RelayFoldersDao = new RelayFoldersDao) {

  def save(folderId: FolderId, parentFolderId: FolderId): Try[Int] =
    relayFoldersDao.insert(folderId.value, parentFolderId.value)
}
