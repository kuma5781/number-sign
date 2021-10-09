package domain.`object`.folder

import domain.`object`.folder.Folder.FolderDto
import domain.`object`.folder.FoldersNotes.FoldersNotesDto
import domain.`object`.note.Note
import domain.`object`.note.Note.NoteDto

case class FoldersNotes(
    folders: Seq[Folder],
    notes: Seq[Note]
) {
  def dto: FoldersNotesDto =
    FoldersNotesDto(
      folders.map(_.dto),
      notes.map(_.dto)
    )
}

object FoldersNotes {
  def apply(folders: Seq[Folder], notes: Seq[Note]): FoldersNotes =
    new FoldersNotes(
      sortFolders(folders),
      notes
    )

  private def sortFolders(folders: Seq[Folder]): Seq[Folder] =
    getChildFolders(Seq(None), folders)

  private def getChildFolders(
      parentFolderIds: Seq[Option[FolderId]],
      folders: Seq[Folder]
  ): Seq[Folder] = {
    val childFolders = parentFolderIds.flatMap { parentFolderId =>
      folders.filter(_.parentFolderId == parentFolderId)
    }
    if (childFolders.isEmpty) {
      childFolders
    } else {
      childFolders ++ getChildFolders(childFolders.map(folder => Some(folder.id)), folders)
    }
  }

  case class FoldersNotesDto(
      folders: Seq[FolderDto],
      notes: Seq[NoteDto]
  )
}
