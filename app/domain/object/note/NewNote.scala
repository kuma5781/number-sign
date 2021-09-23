package domain.`object`.note

import domain.`object`.user.UserId

case class NewNote(
    userId: UserId,
    title: Title,
    content: NoteContent
)

object NewNote {
  def apply(newNoteDto: NewNoteDto): NewNote =
    NewNote(
      UserId(newNoteDto.userId),
      Title(newNoteDto.title),
      NoteContent(newNoteDto.content)
    )

  case class NewNoteDto(
      userId: Int,
      title: String,
      content: String
  )
}
