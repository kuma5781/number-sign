package repository.dao

import java.sql.ResultSet

import domain.`object`.note.NewNote.NewNoteDto
import domain.`object`.note.Note.NoteDto
import domain.`object`.note.NoteStatus.{ Active, Trashed }
import org.scalatestplus.play.PlaySpec
import testSupport.DBSupport

class NoteDaoSpec extends PlaySpec {

  trait Context {
    val noteDao = new NoteDao
    val tableName = "note"

    val userId = 1
    val title = "title"
    val content = "content"
    val newNoteDto = NewNoteDto(userId, title, content)

    val noteDto = (rs: ResultSet) => {
      val id = rs.getInt("id")
      val userId = rs.getInt("user_id")
      val title = rs.getString("title")
      val content = rs.getString("content")
      val status = rs.getString("status")
      NoteDto(id, userId, title, content, status)
    }

    val selectAllSql = s"select * from $tableName"
    val insertSql = (newNoteDto: NewNoteDto) => s"""
         |insert into $tableName (user_id, title, content) values
         |('${newNoteDto.userId}', '${newNoteDto.title}', '${newNoteDto.content}')
			""".stripMargin
  }

  "#insert" should {
    "insert user record" in new Context {
      DBSupport.dbTest(
        tableName, {
          noteDao.insert(newNoteDto)

          val noteDtos = DBAccessor.selectRecords(selectAllSql, noteDto).get

          noteDtos(0).userId mustBe userId
          noteDtos(0).title mustBe title
          noteDtos(0).content mustBe content
          noteDtos(0).status mustBe Active.value
        }
      )
    }
  }

  "#updateStatus" should {
    "update note.status record associated with noteId" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newNoteDto))
          val beforeNoteDtos = DBAccessor.selectRecords(selectAllSql, noteDto).get

          beforeNoteDtos(0).status mustBe Active.value
          val noteId = beforeNoteDtos(0).id

          noteDao.updateStatus(noteId, Trashed.value)

          val updatedNoteDtos = DBAccessor.selectRecords(selectAllSql, noteDto).get
          updatedNoteDtos(0).status mustBe Trashed.value
        }
      )
    }
  }
}
