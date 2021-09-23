package repository

import domain.`object`.folder.{ FolderId, RelayFolders }
import domain.`object`.folder.RelayFolders.RelayFoldersDto
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.dao.RelayFoldersDao

import scala.util.{ Failure, Success }

class RelayFoldersRepositorySpec extends PlaySpec with MockitoSugar {

  trait Context {
    val relayFoldersDao = mock[RelayFoldersDao]
    val relayFoldersRepository = new RelayFoldersRepository(relayFoldersDao)

    val folderIdDto = 1
    val parentFolderIdDto = 2

    val folderId = FolderId(folderIdDto)
    val parentFolderId = FolderId(parentFolderIdDto)

    val relayFoldersDto = RelayFoldersDto(folderIdDto, parentFolderIdDto)
    val relayFolders = RelayFolders(folderId, parentFolderId)
  }

  "#findAllBy" should {
    "return RelayFolders associated with parentFolderIds" in new Context {
      relayFoldersDao.selectAllBy(Seq(parentFolderIdDto)) returns Success(Seq(relayFoldersDto))
      relayFoldersRepository.findAllBy(Seq(parentFolderId)) returns Success(Seq(relayFolders))
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      relayFoldersDao.selectAllBy(Seq(parentFolderIdDto)) returns Failure(exception)
      relayFoldersRepository.findAllBy(Seq(parentFolderId)) returns Failure(exception)
    }
  }

  "#save" should {
    "return Success" in new Context {
      relayFoldersDao.insert(folderIdDto, parentFolderIdDto) returns Success(1)
      relayFoldersRepository.save(folderId, parentFolderId) returns Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      relayFoldersDao.insert(folderIdDto, parentFolderIdDto) returns Failure(exception)
      relayFoldersRepository.save(folderId, parentFolderId) returns Failure(exception)
    }
  }

  "#removeBy" should {
    "return Success" in new Context {
      relayFoldersDao.deleteBy(Seq(parentFolderIdDto)) returns Success(1)
      relayFoldersRepository.removeBy(Seq(parentFolderId)) returns Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      relayFoldersDao.deleteBy(Seq(parentFolderIdDto)) returns Failure(exception)
      relayFoldersRepository.removeBy(Seq(parentFolderId)) returns Failure(exception)
    }
  }
}
