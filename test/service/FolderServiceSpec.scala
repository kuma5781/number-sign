package service

import domain.`object`.folder.{ Folder, FolderId, FolderName, FoldersNotes, NewFolder, RelayFolders }
import domain.`object`.note.NoteStatus.{ Active, Trashed }
import domain.`object`.note.{ Note, NoteContent, NoteId, RelayNoteFolder, Title }
import domain.`object`.user.UserId
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.{ FolderRepository, NoteRepository, RelayFoldersRepository, RelayNoteFolderRepository }

import scala.util.{ Failure, Success }

class FolderServiceSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val folderRepository = mock[FolderRepository]
    val noteRepository = mock[NoteRepository]
    val relayFoldersRepository = mock[RelayFoldersRepository]
    val relayNoteFolderRepository = mock[RelayNoteFolderRepository]
    val folderService =
      new FolderService(
        folderRepository,
        noteRepository,
        relayFoldersRepository,
        relayNoteFolderRepository
      )

    val folderId1 = FolderId(3)
    val userId = UserId(1)
    val folderName1 = FolderName("name1")
    val folder1 = Folder(folderId1, userId, folderName1, None)
    val newFolder1 = NewFolder(userId, folderName1, None)

    val folderId2 = FolderId(4)
    val folderName2 = FolderName("name2")
    val parentFolderId2 = FolderId(3)
    val folder2 = Folder(folderId2, userId, folderName2, Some(parentFolderId2))
    val newFolder2 = NewFolder(userId, folderName2, Some(parentFolderId2))

    val relayFolders1 = RelayFolders(folderId2, folderId1)

    val noteId1 = NoteId(1)
    val title1 = Title("title1")
    val content1 = NoteContent("content1")
    val status1 = Active
    val note1 = Note(noteId1, userId, title1, content1, status1, None)
    val relayNoteFolder1 = RelayNoteFolder(noteId1, folderId1)
  }

  "findFoldersNotesBy" should {
    "return FoldersNotes associated with userId" in new Context {
      folderRepository.findAllBy(userId) returns Success(Seq(folder2, folder1))
      noteRepository.findAllBy(userId) returns Success(Seq(note1))
      folderService.findFoldersNotesBy(userId) mustBe Success(FoldersNotes(Seq(folder1, folder2), Seq(note1)))
    }
    "return Exception" in new Context {
      val exception = new Exception("DB connection error")
      folderRepository.findAllBy(userId) returns Failure(exception)
      folderService.findFoldersNotesBy(userId) mustBe Failure(exception)
    }
  }

  "#save" should {
    "return Success when newFolder doesn't have parentFolderId" in new Context {
      folderRepository.saveAndGetFolderId(newFolder1) returns Success(folderId1)
      folderService.save(newFolder1) mustBe Success(1)
    }

    "return Success when newFolder has parentFolderId" in new Context {
      folderRepository.saveAndGetFolderId(newFolder2) returns Success(folderId2)
      relayFoldersRepository.save(folderId2, parentFolderId2) returns Success(2)
      folderService.save(newFolder2) mustBe Success(2)
    }

    "return Exception when newFolder doesn't have parentFolderId" in new Context {
      val exception = new Exception("DB connection error")
      folderRepository.saveAndGetFolderId(newFolder1) returns Failure(exception)
      folderService.save(newFolder1) mustBe Failure(exception)
    }

    "return Exception when newFolder has parentFolderId" in new Context {
      val exception = new Exception("DB connection error")
      folderRepository.saveAndGetFolderId(newFolder2) returns Success(folderId2)
      relayFoldersRepository.save(folderId2, parentFolderId2) returns Failure(exception)
      folderService.save(newFolder2) mustBe Failure(exception)
    }
  }

  "#updateName" should {
    "return Success" in new Context {
      folderRepository.updateName(folderId1, folderName1) returns Success(1)
      folderService.updateName(folderId1, folderName1) returns Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      folderRepository.updateName(folderId1, folderName1) returns Failure(exception)
      folderService.updateName(folderId1, folderName1) returns Failure(exception)
    }
  }

  "#removeBy" should {
    "return Success" in new Context {
      relayFoldersRepository.findAllBy(Seq(folderId1)) returns Success(Seq(relayFolders1))
      relayFoldersRepository.findAllBy(Seq(folderId2)) returns Success(Seq.empty)
      relayNoteFolderRepository.findAllBy(Seq(folderId1, folderId2)) returns Success(Seq(relayNoteFolder1))
      relayFoldersRepository.removeBy(Seq(folderId1, folderId2)) returns Success(1)
      relayNoteFolderRepository.removeBy(Seq(noteId1)) returns Success(1)
      folderRepository.removeBy(Seq(folderId1, folderId2)) returns Success(1)
      noteRepository.updateStatus(Seq(noteId1), Trashed) returns Success(1)

      folderService.removeBy(folderId1) mustBe Success(4)
    }

    "return Exception" in new Context {
      val exception = new Exception("DB connection error")
      relayFoldersRepository.findAllBy(Seq(folderId1)) returns Failure(exception)

      folderService.removeBy(folderId1) mustBe Failure(exception)
    }
  }
}
