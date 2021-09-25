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

  def updateTitleAndContent(noteId: Int, title:String, content: String): Try[Int] = {
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
