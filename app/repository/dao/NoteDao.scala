package repository.dao

import domain.`object`.note.NewNote.NewNoteDto

import scala.util.Try

class NoteDao {

  private val tableName = "note"

  def insertAndGetId(newNoteDto: NewNoteDto): Try[Int] = {
    val sql =
      s"""
		     |insert into $tableName (user_id, title, content) values
		     |(${newNoteDto.userId}, '${newNoteDto.title}', '${newNoteDto.content}')
			""".stripMargin
    DBAccessor.executeAndGetId(sql)
  }

  def updateStatus(noteId: Int, noteStatus: String): Try[Int] = {
    val sql = s"update $tableName set status = '$noteStatus' where id = $noteId"
    DBAccessor.execute(sql)
  }
}
