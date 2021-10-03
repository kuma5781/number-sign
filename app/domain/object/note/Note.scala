package domain.`object`.note

import domain.`object`.folder.FolderId
import domain.`object`.user.UserId

case class Note(
    id: NoteId,
    userId: UserId,
    title: Title,
    content: NoteContent,
    status: NoteStatus,
    parentFolderId: Option[FolderId]
)

object Note {
  def apply(noteDto: NoteDto): Option[Note] =
    for {
      status <- NoteStatus(noteDto.status)
    } yield
      Note(
        NoteId(noteDto.id),
        UserId(noteDto.userId),
        Title(noteDto.title),
        NoteContent(noteDto.content),
        status,
        noteDto.parentFolderId.map(FolderId)
      )

  case class NoteDto(
      id: Int,
      userId: Int,
      title: String,
      content: String,
      status: String,
      parentFolderId: Option[Int]
  )
}
