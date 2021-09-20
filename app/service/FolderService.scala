package service

import domain.`object`.folder.NewFolder
import repository.{ FolderRepository, RelayFoldersRepository }

import scala.util.{ Failure, Success, Try }

class FolderService(
    folderRepository: FolderRepository = new FolderRepository,
    relayFoldersRepository: RelayFoldersRepository = new RelayFoldersRepository
) {

  def save(newFolder: NewFolder): Try[Int] =
    folderRepository.saveAndGetFolderId(newFolder) match {
      case Success(folderId) =>
        newFolder.parentFolderId match {
          case Some(parentFolderId) => relayFoldersRepository.save(folderId, parentFolderId)
          case None => Success(1)
        }
      case Failure(e) => Failure(e)
    }
}
