package domain.`object`.note

import domain.`object`.folder.FolderId
import domain.`object`.user.UserId

case class NewNote(
    userId: UserId,
    title: Title,
    content: NoteContent,
    parentFolderId: Option[FolderId]
)

object NewNote {
  def apply(newNoteDto: NewNoteDto): NewNote =
    NewNote(
      UserId(newNoteDto.userId),
      Title(newNoteDto.title),
      NoteContent(newNoteDto.content),
      newNoteDto.parentFolderId.map(FolderId)
    )

  case class NewNoteDto(
      userId: Int,
      title: String,
      content: String,
      parentFolderId: Option[Int]
  )
}
