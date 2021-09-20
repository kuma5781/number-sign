package repository

import domain.`object`.note.{ NewNote, NoteId, NoteStatus }
import domain.`object`.note.NewNote.NewNoteDto
import repository.dao.NoteDao

import scala.util.Try

class NoteRepository(noteDao: NoteDao = new NoteDao) {

  def saveAndGetNoteId(newNote: NewNote): Try[NoteId] = {
    val newNoteDto = NewNoteDto(
      newNote.userId.value,
      newNote.title.value,
      newNote.content.value,
      None
    )
    noteDao.insertAndGetId(newNoteDto).map(NoteId)
  }

  def updateStatus(noteId: NoteId, noteStatus: NoteStatus): Try[Int] =
    noteDao.updateStatus(noteId.value, noteStatus.value)

  def removeBy(noteIds: Seq[NoteId]): Try[Int] =
    noteDao.deleteBy(noteIds.map(_.value))
}
