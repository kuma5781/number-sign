package repository

import domain.`object`.note.{ NewNote, NoteId, NoteStatus }
import domain.`object`.note.NewNote.NewNoteDto
import repository.dao.NoteDao

import scala.util.Try

class NoteRepository(noteDao: NoteDao = new NoteDao) {

  def save(newNote: NewNote): Try[Int] = {
    val newNoteDto = NewNoteDto(
      newNote.userId.value,
      newNote.title.value,
      newNote.content.value
    )
    noteDao.insert(newNoteDto)
  }

  def updateStatus(noteId: NoteId, noteStatus: NoteStatus): Try[Int] =
    noteDao.updateStatus(noteId.value, noteStatus.value)
}
