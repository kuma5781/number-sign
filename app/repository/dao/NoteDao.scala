package repository.dao

import java.sql.ResultSet

import domain.`object`.note.NewNote.NewNoteDto
import domain.`object`.note.Note.NoteDto

import scala.util.Try

class NoteDao {

  private val tableName = "note"
  private val relayNoteFolderTableName = "relay_note_folder"

  private val noteDto = (rs: ResultSet) => {
    val id = rs.getInt("id")
    val userId = rs.getInt("user_id")
    val title = rs.getString("title")
    val content = rs.getString("content")
    val status = rs.getString("status")
    NoteDto(id, userId, title, content, status, None)
  }

  private val noteDtoWithParent = (rs: ResultSet) => {
    val id = rs.getInt("id")
    val userId = rs.getInt("user_id")
    val title = rs.getString("title")
    val content = rs.getString("content")
    val status = rs.getString("status")
    val parentFolderId = rs.getInt("parent_folder_id") match {
      case 0 => None
      case num => Some(num)
    }
    NoteDto(id, userId, title, content, status, parentFolderId)
  }

  def selectBy(noteId: Int): Try[NoteDto] = {
    val sql = s"select * from $tableName where id = $noteId"
    DBAccessor.selectRecord(sql, noteDto)
  }

  def selectAllByUserId(userId: Int): Try[Seq[NoteDto]] = {
    val sql =
      s"""
         |select n.id, user_id, title, content, status, parent_folder_id
         |from $tableName as n
         |left join $relayNoteFolderTableName as rnf
         |on n.id = rnf.note_id
         |where user_id = $userId
      """.stripMargin
    DBAccessor.selectRecords(sql, noteDtoWithParent)
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
