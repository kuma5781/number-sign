package service

import domain.`object`.folder.{ FolderId, NewFolder }
import domain.`object`.note.NoteStatus.Trashed
import repository.{ FolderRepository, NoteRepository, RelayFoldersRepository, RelayNoteFolderRepository }

import scala.util.control.Breaks.break
import scala.util.{ Failure, Success, Try }

class FolderService(
    folderRepository: FolderRepository = new FolderRepository,
    noteRepository: NoteRepository = new NoteRepository,
    relayFoldersRepository: RelayFoldersRepository = new RelayFoldersRepository,
    relayNoteFolderRepository: RelayNoteFolderRepository = new RelayNoteFolderRepository
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

  def removeBy(folderId: FolderId): Try[Int] = {
    //削除するfolder内のfolderIdを全取得
    findAllChildFolders(Seq(folderId)) match {
      case Success(folderIds) =>
        //削除するfolder内のnoteIdを全取得
        relayNoteFolderRepository.findAllBy(folderIds) match {
          case Success(relays) =>
            val noteIds = relays.map(_.noteId)
            //削除するfolder内のrelayFoldersを全削除
            relayFoldersRepository.removeBy(folderIds) match {
              case Success(_) =>
                //削除するfolder内のrelayNoteFolderを全削除
                relayNoteFolderRepository.removeBy(noteIds) match {
                  case Success(_) =>
                    //削除するfolderとfolder内のfolderを全削除
                    folderRepository.removeBy(folderIds) match {
                      //削除するfolder内のnoteを全てゴミ箱に入れる
                      case Success(_) => noteRepository.updateStatus(noteIds, Trashed)
                      case Failure(e) => Failure(e)
                    }
                  case Failure(e) => Failure(e)
                }
              case Failure(e) => Failure(e)
            }
          case Failure(e) => Failure(e)
        }
      case Failure(e) => Failure(e)
    }
  }

  def findAllChildFolders(parentFolderIds: Seq[FolderId]): Try[Seq[FolderId]] = {
    relayFoldersRepository.findAllBy(parentFolderIds) match {
      case Success(relays) if relays.map(_.folderId) == parentFolderIds => Success(parentFolderIds)
      case Success(relays) => findAllChildFolders(relays.map(_.folderId))
      case Failure(e) => Failure(e)
    }
  }
}
