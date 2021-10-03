package repository

import domain.`object`.folder.Folder.FolderDto
import domain.`object`.folder.NewFolder.NewFolderDto
import domain.`object`.folder.{ Folder, FolderId, FolderName, NewFolder }
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
    val folderDto = FolderDto(folderIdDto, userIdDto, folderNameDto, None)
    val newFolderDto = NewFolderDto(userIdDto, folderNameDto, None)

    val folderId = FolderId(folderIdDto)
    val userId = UserId(userIdDto)
    val folderName = FolderName(folderNameDto)
    val folder = Folder(folderId, userId, folderName, None)
    val newFolder = NewFolder(userId, folderName, None)
  }

  "#findAllBy" should {
    "return all Folders associated with userId" in new Context {
      folderDao.selectAllByUserId(userIdDto) returns Success(Seq(folderDto))
      folderRepository.findAllBy(userId) mustBe Success(Seq(folder))
    }

    "return Exception" in new Context {
      val exception = new Exception("DB connection error")
      folderDao.selectAllByUserId(userIdDto) returns Failure(exception)
      folderRepository.findAllBy(userId) mustBe Failure(exception)
    }
  }

  "#saveAndGetFolderId" should {
    "return last inserted folderId" in new Context {
      folderDao.insertAndGetId(newFolderDto) returns Success(folderIdDto)
      folderRepository.saveAndGetFolderId(newFolder) returns Success(folderId)
    }

    "return Exception" in new Context {
      val exception = new Exception("DB connection error")
      folderDao.insertAndGetId(newFolderDto) returns Failure(exception)
      folderRepository.saveAndGetFolderId(newFolder) returns Failure(exception)
    }
  }

  "#updateName" should {
    "return Success" in new Context {
      folderDao.updateName(folderIdDto, folderNameDto) returns Success(1)
      folderRepository.updateName(folderId, folderName) returns Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      folderDao.updateName(folderIdDto, folderNameDto) returns Failure(exception)
      folderRepository.updateName(folderId, folderName) returns Failure(exception)
    }
  }

  "#removeBy" should {
    "return Success" in new Context {
      folderDao.deleteBy(Seq(folderIdDto)) returns Success(1)
      folderRepository.removeBy(Seq(folderId)) returns Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception("DB connection error")
      folderDao.deleteBy(Seq(folderIdDto)) returns Failure(exception)
      folderRepository.removeBy(Seq(folderId)) returns Failure(exception)
    }
  }
}
