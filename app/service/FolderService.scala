package service

import domain.`object`.folder.{ FolderId, NewFolder }
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

  def remove(folderId: FolderId): Try[Int] = {
    var parentFolderIds = Seq(folderId)
    var folderIds = Seq(folderId)
    //削除するfolder内のfolderIdを全取得
    while (true) {
      relayFoldersRepository.findAllBy(parentFolderIds) match {
        case Success(relays) if relays.isEmpty =>
          folderIds = folderIds ++ relays.map(_.folderId)
          break
        case Success(relays) =>
          parentFolderIds = relays.map(_.folderId) diff folderIds
          folderIds = folderIds ++ relays.map(_.folderId)
        case Failure(e) => return Failure(e)
      }
    }
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
                  //削除するfolder内のnoteを全削除
                  case Success(_) => noteRepository.removeBy(noteIds)
                  case Failure(e) => Failure(e)
                }
              case Failure(e) => Failure(e)
            }
          case Failure(e) => Failure(e)
        }
      case Failure(e) => Failure(e)
    }
  }
}
