package repository.dao

import java.sql.ResultSet

import domain.`object`.note.NewNote.NewNoteDto
import domain.`object`.note.Note.NoteDto

import scala.util.Try

class NoteDao {

  private val tableName = "note"

  private val noteDto = (rs: ResultSet) => {
    val id = rs.getInt("id")
    val userId = rs.getInt("user_id")
    val title = rs.getString("title")
    val content = rs.getString("content")
    val status = rs.getString("status")
    NoteDto(id, userId, title, content, status)
  }

  def selectBy(noteId: Int): Try[NoteDto] = {
    val sql = s"select * from $tableName where id = $noteId"
    DBAccessor.selectRecord(sql, noteDto)
  }

  def insertAndGetId(newNoteDto: NewNoteDto): Try[Int] = {
    val sql =
      s"""
		     |insert into $tableName (user_id, title, content) values
		     |(${newNoteDto.userId}, '${newNoteDto.title}', '${newNoteDto.content}')
			""".stripMargin
    DBAccessor.executeAndGetId(sql)
  }

  def updateTitleAndContent(noteId: Int, title: String, content: String): Try[Int] = {
    val sql = s"update $tableName set title = '$title', content = '$content' where id = $noteId"
    DBAccessor.execute(sql)
  }

  def updateStatus(noteId: Int, noteStatus: String): Try[Int] = {
    val sql = s"update $tableName set status = '$noteStatus' where id = $noteId"
    DBAccessor.execute(sql)
  }

  def updateStatus(noteIds: Seq[Int], noteStatus: String): Try[Int] = {
    val sql = s"update $tableName set status = '$noteStatus' where id in (${noteIds.mkString(",")})"
    DBAccessor.execute(sql)
  }
}
