package service

import domain.`object`.note.NewNote
import repository.NoteRepository

import scala.util.Try

class NoteService(noteRepository: NoteRepository = new NoteRepository) {

  def save(newNote: NewNote): Try[Int] = noteRepository.save(newNote)
}
