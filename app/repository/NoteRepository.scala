package repository

import domain.`object`.note.{ NewNote, Note, NoteContent, NoteId, NoteStatus, Title }
import domain.`object`.note.NewNote.NewNoteDto
import repository.dao.NoteDao

import scala.util.{ Failure, Success, Try }

class NoteRepository(noteDao: NoteDao = new NoteDao) {

  def findBy(noteId: NoteId): Try[Note] =
    noteDao
      .selectBy(noteId.value)
      .flatMap(Note(_).fold[Try[Note]](Failure(new Exception("Failed to get Note")))(Success(_)))

  def saveAndGetNoteId(newNote: NewNote): Try[NoteId] = {
    val newNoteDto = NewNoteDto(
      newNote.userId.value,
      newNote.title.value,
      newNote.content.value,
      None
    )
    noteDao.insertAndGetId(newNoteDto).map(NoteId)
  }

  def updateTitleAndContent(noteId: NoteId, title: Title, content: NoteContent): Try[Int] =
    noteDao.updateTitleAndContent(noteId.value, title.value, content.value)

  def updateStatus(noteId: NoteId, noteStatus: NoteStatus): Try[Int] =
    noteDao.updateStatus(noteId.value, noteStatus.value)

  def updateStatus(noteIds: Seq[NoteId], noteStatus: NoteStatus): Try[Int] =
    noteDao.updateStatus(noteIds.map(_.value), noteStatus.value)
}
