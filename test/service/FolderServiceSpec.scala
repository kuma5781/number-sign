package service

import domain.`object`.folder.NewFolder.NewFolderDto
import domain.`object`.folder.{ FolderId, FolderName, NewFolder }
import domain.`object`.user.UserId
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.{ FolderRepository, RelayFoldersRepository }

import scala.util.{ Failure, Success }

class FolderServiceSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val folderRepository = mock[FolderRepository]
    val relayFoldersRepository = mock[RelayFoldersRepository]
    val folderService = new FolderService(folderRepository, relayFoldersRepository)

    val folderId1 = FolderId(3)
    val userId1 = UserId(1)
    val folderName1 = FolderName("name1")
    val newFolder1 = NewFolder(userId1, folderName1, None)

    val folderId2 = FolderId(4)
    val userId2 = UserId(2)
    val folderName2 = FolderName("name2")
    val parentFolderId2 = FolderId(5)
    val newFolder2 = NewFolder(userId2, folderName2, Some(parentFolderId2))
  }

  "#save" should {
    "return Success when newFolder has parentFolderId" in new Context {
      folderRepository.saveAndGetFolderId(newFolder1) returns Success(folderId1)
      folderService.save(newFolder1) mustBe Success(1)
    }

    "return Success when newFolder doesn't have parentFolderId" in new Context {
      folderRepository.saveAndGetFolderId(newFolder2) returns Success(folderId2)
      relayFoldersRepository.save(folderId2, parentFolderId2) returns Success(2)
      folderService.save(newFolder2) mustBe Success(2)
    }

    "return Exception when newFolder has parentFolderId" in new Context {
      val exception = new Exception(s"DB connection error")
      folderRepository.saveAndGetFolderId(newFolder1) returns Failure(exception)
      folderService.save(newFolder1) mustBe Failure(exception)
    }

    "return Exception when newFolder doesn't have parentFolderId" in new Context {
      val exception = new Exception(s"DB connection error")
      folderRepository.saveAndGetFolderId(newFolder2) returns Success(folderId2)
      relayFoldersRepository.save(folderId2, parentFolderId2) returns Failure(exception)
      folderService.save(newFolder2) mustBe Failure(exception)
    }
  }
}
