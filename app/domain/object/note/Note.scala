package domain.`object`.note

import domain.`object`.user.UserId

case class Note(
    id: NoteId,
    userId: UserId,
    title: Title,
    content: NoteContent,
    status: NoteStatus
)

object Note {
  case class NoteDto(
      id: Int,
      userId: Int,
      title: String,
      content: String,
      status: String
  )
}
