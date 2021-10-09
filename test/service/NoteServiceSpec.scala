package service

import domain.`object`.folder.FolderId
import domain.`object`.note.NoteStatus.{ Active, Trashed }
import domain.`object`.note.{ NewNote, Note, NoteContent, NoteId, Title }
import domain.`object`.user.UserId
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import play.api.libs.json.Json
import repository.{ NoteRepository, RelayNoteFolderRepository }

import scala.util.{ Failure, Success }

class NoteServiceSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val noteRepository = mock[NoteRepository]
    val relayNoteFolderRepository = mock[RelayNoteFolderRepository]
    val noteService = new NoteService(noteRepository, relayNoteFolderRepository)

    val noteId1 = NoteId(3)
    val userId1 = UserId(1)
    val title1 = Title("title1")
    val content1 = NoteContent("content1")
    val status1 = Active
    val note1 = Note(noteId1, userId1, title1, content1, status1, None)
    val newNote1 = NewNote(userId1, title1, content1, None)

    val noteId2 = NoteId(4)
    val userId2 = UserId(2)
    val title2 = Title("title2")
    val content2 = NoteContent("content2")
    val parentFolderId2 = FolderId(1)
    val newNote2 = NewNote(userId2, title2, content2, Some(parentFolderId2))
  }

  "findBy" should {
    "return Note associated with noteId" in new Context {
      noteRepository.findBy(noteId1) returns Success(note1)
      noteService.findBy(noteId1) mustBe Success(note1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      noteRepository.findBy(noteId1) returns Failure(exception)
      noteService.findBy(noteId1) mustBe Failure(exception)
    }
  }

  "#save" should {
    "return Success when newNote doesn't have parentFolderId" in new Context {
      noteRepository.saveAndGetNoteId(newNote1) returns Success(noteId1)
      noteService.save(newNote1) mustBe Success(1)
    }

    "return Success when newNote has parentFolderId" in new Context {
      noteRepository.saveAndGetNoteId(newNote2) returns Success(noteId2)
      relayNoteFolderRepository.save(noteId2, parentFolderId2) returns Success(2)
      noteService.save(newNote2) mustBe Success(2)
    }

    "return Exception when newNote doesn't have parentFolderId" in new Context {
      val exception = new Exception("DB connection error")
      noteRepository.saveAndGetNoteId(newNote1) returns Failure(exception)
      noteService.save(newNote1) mustBe Failure(exception)
    }

    "return Exception when newNote has parentFolderId" in new Context {
      val exception = new Exception("DB connection error")
      noteRepository.saveAndGetNoteId(newNote2) returns Success(noteId2)
      relayNoteFolderRepository.save(noteId2, parentFolderId2) returns Failure(exception)
      noteService.save(newNote2) mustBe Failure(exception)
    }
  }

  "#updateTitleAndContent" should {
    "return Success" in new Context {
      noteRepository.updateTitleAndContent(noteId1, title1, content1) returns Success(1)
      noteService.updateTitleAndContent(noteId1, title1, content1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      noteRepository.updateTitleAndContent(noteId1, title1, content1) returns Failure(exception)
      noteService.updateTitleAndContent(noteId1, title1, content1) mustBe Failure(exception)
    }
  }

  "#trash" should {
    "return Success" in new Context {
      noteRepository.updateStatus(noteId1, Trashed) returns Success(1)
      noteService.trash(noteId1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception("DB connection error")
      noteRepository.updateStatus(noteId1, Trashed) returns Failure(exception)
      noteService.trash(noteId1) mustBe Failure(exception)
    }
  }

  "#activate" should {
    "return Success" in new Context {
      noteRepository.updateStatus(noteId1, Active) returns Success(1)
      noteService.activate(noteId1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception("DB connection error")
      noteRepository.updateStatus(noteId1, Active) returns Failure(exception)
      noteService.activate(noteId1) mustBe Failure(exception)
    }
  }
}
