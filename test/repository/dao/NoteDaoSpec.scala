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

    val userId1 = 1
    val title1 = "title1"
    val content1 = "content1"
    val newNoteDto1 = NewNoteDto(userId1, title1, content1, None)

    val userId2 = 2
    val title2 = "title2"
    val content2 = "content2"
    val newNoteDto2 = NewNoteDto(userId2, title2, content2, None)

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

  "#selectBy" should {
    "return a note record associated with noteId" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newNoteDto1))

          val noteDtos = DBAccessor.selectRecords(selectAllSql, noteDto).get
          val noteId = noteDtos(0).id

          val selectNote = noteDao.selectBy(noteId).get
          selectNote.userId mustBe userId1
          selectNote.title mustBe title1
          selectNote.content mustBe content1
        }
      )
    }
  }

  "#insertAndGetId" should {
    "insert note record and return last inserted id" in new Context {
      DBSupport.dbTest(
        tableName, {
          val noteId = noteDao.insertAndGetId(newNoteDto1).get

          val noteDtos = DBAccessor.selectRecords(selectAllSql, noteDto).get

          noteDtos(0).id mustBe noteId
          noteDtos(0).userId mustBe userId1
          noteDtos(0).title mustBe title1
          noteDtos(0).content mustBe content1
          noteDtos(0).status mustBe Active.value
        }
      )
    }
  }

  "#updateTitleAndContent" should {
    "update title and content of note associated with noteId" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newNoteDto1))
          val beforeNoteDtos = DBAccessor.selectRecords(selectAllSql, noteDto).get

          beforeNoteDtos(0).title mustBe title1
          beforeNoteDtos(0).content mustBe content1
          val noteId = beforeNoteDtos(0).id

          noteDao.updateTitleAndContent(noteId, title2, content2)

          val updatedNoteDtos = DBAccessor.selectRecords(selectAllSql, noteDto).get
          updatedNoteDtos(0).title mustBe title2
          updatedNoteDtos(0).content mustBe content2
        }
      )
    }
  }

  "#updateStatus(noteId: Int, noteStatus: String)" should {
    "update note.status record associated with noteId" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newNoteDto1))
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

  "#updateStatus(noteIds: Seq[Int], noteStatus: String)" should {
    "update note.status records associated with noteIds" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newNoteDto1))
          DBAccessor.execute(insertSql(newNoteDto2))
          val beforeNoteDtos = DBAccessor.selectRecords(selectAllSql, noteDto).get

          beforeNoteDtos(0).status mustBe Active.value
          beforeNoteDtos(1).status mustBe Active.value
          val noteIds = beforeNoteDtos.map(_.id)

          noteDao.updateStatus(noteIds, Trashed.value)

          val updatedNoteDtos = DBAccessor.selectRecords(selectAllSql, noteDto).get
          updatedNoteDtos(0).status mustBe Trashed.value
          updatedNoteDtos(1).status mustBe Trashed.value
        }
      )
    }
  }
}
