package repository

import domain.`object`.folder.FolderId
import domain.`object`.note.NoteId
import repository.dao.RelayNoteFolderDao

import scala.util.Try

class RelayNoteFolderRepository(relayNoteFolderDao: RelayNoteFolderDao = new RelayNoteFolderDao) {

  def save(noteId: NoteId, parentFolderId: FolderId): Try[Int] =
    relayNoteFolderDao.insert(noteId.value, parentFolderId.value)
}
