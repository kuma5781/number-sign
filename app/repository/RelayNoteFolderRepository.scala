package repository

import domain.`object`.folder.FolderId
import domain.`object`.note.{ NoteId, RelayNoteFolder }
import repository.dao.RelayNoteFolderDao

import scala.util.Try

class RelayNoteFolderRepository(relayNoteFolderDao: RelayNoteFolderDao = new RelayNoteFolderDao) {

  def findAllBy(parentFolderIds: Seq[FolderId]): Try[Seq[RelayNoteFolder]] = {
    relayNoteFolderDao.selectAllBy(parentFolderIds.map(_.value)).map { dtos =>
      dtos.map(RelayNoteFolder(_))
    }
  }

  def save(noteId: NoteId, parentFolderId: FolderId): Try[Int] =
    relayNoteFolderDao.insert(noteId.value, parentFolderId.value)

  def removeBy(noteIds: Seq[NoteId]): Try[Int] =
    relayNoteFolderDao.deleteBy(noteIds.map(_.value))
}
