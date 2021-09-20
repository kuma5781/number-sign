package service

import domain.`object`.folder.FolderId
import domain.`object`.note.NoteStatus.{Active, Trashed}
import domain.`object`.note.{NewNote, NoteContent, NoteId, Title}
import domain.`object`.user.UserId
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.{NoteRepository, RelayNoteFolderRepository}

import scala.util.{Failure, Success}

class NoteServiceSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val noteRepository = mock[NoteRepository]
    val relayNoteFolderRepository = mock[RelayNoteFolderRepository]
    val noteService = new NoteService(noteRepository, relayNoteFolderRepository)

    val noteId1 = NoteId(3)
    val userId1 = UserId(1)
    val title1 = Title("title1")
    val content1 = NoteContent("content1")
    val newNote1 = NewNote(userId1, title1, content1, None)

    val noteId2 = NoteId(4)
    val userId2 = UserId(2)
    val title2 = Title("title2")
    val content2 = NoteContent("content2")
    val parentFolderId = FolderId(1)
    val newNote2 = NewNote(userId2, title2, content2, Some(parentFolderId))
  }

  "#save" should {
    "return Success when newNote.parentFolderId is not exist" in new Context {
      noteRepository.saveAndGetNoteId(newNote1) returns Success(noteId1)
      noteService.save(newNote1) mustBe Success(1)
    }

    "return Success when newNote.parentFolderId is exist" in new Context {
      noteRepository.saveAndGetNoteId(newNote2) returns Success(noteId2)
      relayNoteFolderRepository.save(noteId2, parentFolderId) returns Success(2)
      noteService.save(newNote2) mustBe Success(2)
    }

    "return Exception when newNote.parentFolderId is not exist" in new Context {
      val exception = new Exception(s"DB connection error")
      noteRepository.saveAndGetNoteId(newNote1) returns Failure(exception)
      noteService.save(newNote1) mustBe Failure(exception)
    }

    "return Exception when newNote.parentFolderId is exist" in new Context {
      val exception = new Exception(s"DB connection error")
      noteRepository.saveAndGetNoteId(newNote2) returns Success(noteId2)
      relayNoteFolderRepository.save(noteId2, parentFolderId) returns Failure(exception)
      noteService.save(newNote2) mustBe Failure(exception)
    }
  }

  "#trash" should {
    "return Success" in new Context {
      noteRepository.updateStatus(noteId1, Trashed) returns Success(1)
      noteService.trash(noteId1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
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
      val exception = new Exception(s"DB connection error")
      noteRepository.updateStatus(noteId1, Active) returns Failure(exception)
      noteService.activate(noteId1) mustBe Failure(exception)
    }
  }
}
