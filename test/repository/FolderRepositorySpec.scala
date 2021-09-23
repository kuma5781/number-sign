package repository

import domain.`object`.folder.{ FolderId, FolderName, NewFolder }
import domain.`object`.folder.NewFolder.NewFolderDto
import domain.`object`.user.UserId
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.dao.FolderDao

import scala.util.{ Failure, Success }

class FolderRepositorySpec extends PlaySpec with MockitoSugar {

  trait Context {
    val folderDao = mock[FolderDao]
    val folderRepository = new FolderRepository(folderDao)

    val folderIdDto = 1
    val userIdDto = 1
    val folderNameDto = "name"
    val newFolderDto = NewFolderDto(userIdDto, folderNameDto, None)

    val folderId = FolderId(folderIdDto)
    val userId = UserId(userIdDto)
    val folderName = FolderName(folderNameDto)
    val newFolder = NewFolder(userId, folderName, None)
  }

  "#saveAndGetFolderId" should {
    "return last inserted folderId" in new Context {
      folderDao.insertAndGetId(newFolderDto) returns Success(folderIdDto)
      folderRepository.saveAndGetFolderId(newFolder) returns Success(folderId)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      folderDao.insertAndGetId(newFolderDto) returns Failure(exception)
      folderRepository.saveAndGetFolderId(newFolder) returns Failure(exception)
    }
  }

  "#removeBy" should {
    "return Success" in new Context {
      folderDao.deleteBy(Seq(folderIdDto)) returns Success(1)
      folderRepository.removeBy(Seq(folderId)) returns Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      folderDao.deleteBy(Seq(folderIdDto)) returns Failure(exception)
      folderRepository.removeBy(Seq(folderId)) returns Failure(exception)
    }
  }
}
