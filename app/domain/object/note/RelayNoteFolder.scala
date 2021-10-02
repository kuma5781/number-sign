package domain.`object`.note

import domain.`object`.folder.FolderId

case class RelayNoteFolder(
    noteId: NoteId,
    parentFolderId: FolderId
)

object RelayNoteFolder {
  def apply(relayNoteFolderDto: RelayNoteFolderDto): RelayNoteFolder =
    RelayNoteFolder(
      NoteId(relayNoteFolderDto.noteId),
      FolderId(relayNoteFolderDto.parentFolderId)
    )

  case class RelayNoteFolderDto(
      noteId: Int,
      parentFolderId: Int
  )
}
