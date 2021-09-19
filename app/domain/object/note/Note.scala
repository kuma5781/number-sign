package domain.`object`.note

import domain.`object`.user.UserId

case class Note(
    id: NoteId,
    userId: UserId,
    title: Title,
    content: NoteContent,
    status: NoteStatus
)
