package service

import domain.`object`.note.{ NewNote, NoteContent, Title }
import domain.`object`.user.UserId
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.NoteRepository

import scala.util.{ Failure, Success }

class NoteServiceSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val noteRepository = mock[NoteRepository]
    val noteService = new NoteService(noteRepository)

    val userId = UserId(1)
    val title = Title("title")
    val content = NoteContent("content")
    val newNote = NewNote(userId, title, content)
  }

  "#save" should {
    "return Success" in new Context {
      noteRepository.save(newNote) returns Success(1)
      noteService.save(newNote) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      noteRepository.save(newNote) returns Failure(exception)
      noteService.save(newNote) mustBe Failure(exception)
    }
  }
}
