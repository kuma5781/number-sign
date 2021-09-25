package service

import domain.`object`.folder.{ FolderId, FolderName, NewFolder }
import domain.`object`.note.NoteStatus.Trashed
import repository.{ FolderRepository, NoteRepository, RelayFoldersRepository, RelayNoteFolderRepository }

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

  def updateName(folderId: FolderId, name: FolderName): Try[Int] =
    folderRepository.updateName(folderId, name)

  def removeBy(folderId: FolderId): Try[Int] =
    for {
      //削除するfolder内のfolderIdを全取得
      folderIds <- findAllChildFolders(Seq(folderId))
      //削除するfolder内のnoteIdを全取得
      relayNoteFolder <- relayNoteFolderRepository.findAllBy(folderIds)
      noteIds = relayNoteFolder.map(_.noteId)
      //削除するfolder内のrelayFoldersを全削除
      removedRelayFolders <- relayFoldersRepository.removeBy(folderIds)
      //削除するfolder内のrelayNoteFolderを全削除
      removedRelayNoteFolder <- relayNoteFolderRepository.removeBy(noteIds)
      //削除するfolderとfolder内のfolderを全削除
      removedFolders <- folderRepository.removeBy(folderIds)
      //削除するfolder内のnoteを全てゴミ箱に入れる
      trashedNote <- noteRepository.updateStatus(noteIds, Trashed)
    } yield {
      removedRelayFolders + removedRelayNoteFolder + removedFolders + trashedNote
    }

  private def findAllChildFolders(parentFolderIds: Seq[FolderId]): Try[Seq[FolderId]] =
    for {
      relays <- relayFoldersRepository.findAllBy(parentFolderIds)
      foundFolderIds = relays.map(_.folderId)
      allChildFolderIds <- if (foundFolderIds.isEmpty) {
        Success(parentFolderIds)
      } else {
        for {
          folderIds <- findAllChildFolders(foundFolderIds)
        } yield {
          parentFolderIds ++ folderIds
        }
      }
    } yield {
      allChildFolderIds
    }
}
