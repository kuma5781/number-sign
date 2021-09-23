package repository

import domain.`object`.folder.FolderId
import domain.`object`.note.RelayNoteFolder.RelayNoteFolderDto
import domain.`object`.note.{ NoteId, RelayNoteFolder }
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.dao.RelayNoteFolderDao

import scala.util.{ Failure, Success }

class RelayNoteFolderRepositorySpec extends PlaySpec with MockitoSugar {

  trait Context {
    val relayNoteFolderDao = mock[RelayNoteFolderDao]
    val relayNoteFolderRepository = new RelayNoteFolderRepository(relayNoteFolderDao)

    val noteIdDto = 1
    val parentFolderIdDto = 1

    val noteId = NoteId(noteIdDto)
    val parentFolderId = FolderId(parentFolderIdDto)

    val relayNoteFolderDto = RelayNoteFolderDto(noteIdDto, parentFolderIdDto)
    val relayNoteFolder = RelayNoteFolder(noteId, parentFolderId)
  }

  "findAllBy" should {
    "return RelayNoteFolder associated with parentFolderIds" in new Context {
      relayNoteFolderDao.selectAllBy(Seq(parentFolderIdDto)) returns Success(Seq(relayNoteFolderDto))
      relayNoteFolderRepository.findAllBy(Seq(parentFolderId)) returns Success(Seq(relayNoteFolder))
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      relayNoteFolderDao.selectAllBy(Seq(parentFolderIdDto)) returns Failure(exception)
      relayNoteFolderRepository.findAllBy(Seq(parentFolderId)) returns Failure(exception)
    }
  }

  "#save" should {
    "return Success" in new Context {
      relayNoteFolderDao.insert(noteIdDto, parentFolderIdDto) returns Success(1)
      relayNoteFolderRepository.save(noteId, parentFolderId) returns Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      relayNoteFolderDao.insert(noteIdDto, parentFolderIdDto) returns Failure(exception)
      relayNoteFolderRepository.save(noteId, parentFolderId) returns Failure(exception)
    }
  }

  "#removeBy" should {
    "return Success" in new Context {
      relayNoteFolderDao.deleteBy(Seq(noteIdDto)) returns Success(1)
      relayNoteFolderRepository.removeBy(Seq(noteId)) returns Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      relayNoteFolderDao.deleteBy(Seq(noteIdDto)) returns Failure(exception)
      relayNoteFolderRepository.removeBy(Seq(noteId)) returns Failure(exception)
    }
  }
}
