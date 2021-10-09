package domain.`object`.note

import domain.`object`.folder.FolderId
import domain.`object`.note.Note.NoteDto
import domain.`object`.user.UserId

case class Note(
    id: NoteId,
    userId: UserId,
    title: Title,
    content: NoteContent,
    status: NoteStatus,
    parentFolderId: Option[FolderId]
) {
  def dto: NoteDto =
    NoteDto(
      id.value,
      userId.value,
      title.value,
      content.value,
      status.value,
      parentFolderId.map(_.value)
    )
}

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
