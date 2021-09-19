package repository.dao

import domain.`object`.note.NewNote.NewNoteDto

import scala.util.Try

class NoteDao {

  private val tableName = "note"

  def insert(newNoteDto: NewNoteDto): Try[Int] = {
    val sql =
      s"""
        |insert into $tableName (user_id, title, content) values
				|('${newNoteDto.userId}', '${newNoteDto.title}', '${newNoteDto.content}')
			""".stripMargin
    DBAccessor.execute(sql)
  }
}
