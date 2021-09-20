package repository

import domain.`object`.note.NewNote.NewNoteDto
import domain.`object`.note.NoteStatus.Active
import domain.`object`.note.{ NewNote, NoteContent, NoteId, Title }
import domain.`object`.user.UserId
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.dao.NoteDao

import scala.util.{ Failure, Success }

class NoteRepositorySpec extends PlaySpec with MockitoSugar {

  trait Context {
    val noteDao = mock[NoteDao]
    val noteRepository = new NoteRepository(noteDao)

    val noteIdDto = 1
    val userIdDto = 1
    val titleDto = "title"
    val contentDto = "content"
    val newNoteDto = NewNoteDto(userIdDto, titleDto, contentDto, None)

    val noteId = NoteId(noteIdDto)
    val userId = UserId(userIdDto)
    val title = Title(titleDto)
    val content = NoteContent(contentDto)
    val newNote = NewNote(userId, title, content, None)
  }

  "#saveAndGetNoteId" should {
    "return last inserted noteId" in new Context {
      noteDao.insertAndGetId(newNoteDto) returns Success(noteIdDto)
      noteRepository.saveAndGetNoteId(newNote) mustBe Success(noteId)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      noteDao.insertAndGetId(newNoteDto) returns Failure(exception)
      noteRepository.saveAndGetNoteId(newNote) mustBe Failure(exception)
    }
  }

  "#updateStatus" should {
    "return Success" in new Context {
      noteDao.updateStatus(noteIdDto, Active.value) returns Success(1)
      noteRepository.updateStatus(noteId, Active) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      noteDao.updateStatus(noteIdDto, Active.value) returns Failure(exception)
      noteRepository.updateStatus(noteId, Active) mustBe Failure(exception)
    }
  }
}
