package service

import domain.`object`.note.NoteStatus.{ Active, Trashed }
import domain.`object`.note.{ NewNote, NoteId }
import repository.NoteRepository

import scala.util.Try

class NoteService(noteRepository: NoteRepository = new NoteRepository) {

  def save(newNote: NewNote): Try[Int] = noteRepository.save(newNote)
  def trash(noteId: NoteId): Try[Int] = noteRepository.updateStatus(noteId, Trashed)
  def activate(noteId: NoteId): Try[Int] = noteRepository.updateStatus(noteId, Active)
}
