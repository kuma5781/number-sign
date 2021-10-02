package service

import domain.`object`.note.NoteStatus.{ Active, Trashed }
import domain.`object`.note.{ NewNote, Note, NoteContent, NoteId, Title }
import repository.{ NoteRepository, RelayNoteFolderRepository }

import scala.util.{ Failure, Success, Try }

class NoteService(
    noteRepository: NoteRepository = new NoteRepository,
    relayNoteFolderRepository: RelayNoteFolderRepository = new RelayNoteFolderRepository
) {

  def findBy(noteId: NoteId): Try[Note] = noteRepository.findBy(noteId)

  def save(newNote: NewNote): Try[Int] =
    noteRepository.saveAndGetNoteId(newNote) match {
      case Success(noteId) =>
        newNote.parentFolderId match {
          case Some(parentFolderId) => relayNoteFolderRepository.save(noteId, parentFolderId)
          case None => Success(1)
        }
      case Failure(e) => Failure(e)
    }

  def updateTitleAndContent(noteId: NoteId, title: Title, content: NoteContent): Try[Int] =
    noteRepository.updateTitleAndContent(noteId, title, content)

  def trash(noteId: NoteId): Try[Int] =
    noteRepository.updateStatus(noteId, Trashed)

  def activate(noteId: NoteId): Try[Int] =
    noteRepository.updateStatus(noteId, Active)
}
