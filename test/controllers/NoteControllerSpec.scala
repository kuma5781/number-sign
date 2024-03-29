package controllers

import domain.`object`.folder.FolderId
import domain.`object`.note.NewNote.NewNoteDto
import domain.`object`.note.NoteStatus.Active
import domain.`object`.note._
import domain.`object`.user.UserId
import org.powermock.reflect.Whitebox
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import play.api.http.Status.{ BAD_REQUEST, OK }
import play.api.libs.functional.syntax.{ toFunctionalBuilderOps, unlift }
import play.api.libs.json.{ JsObject, JsPath, Json, Writes }
import play.api.test.FakeRequest
import play.api.test.Helpers.{ contentAsString, defaultAwaitTimeout, status, stubControllerComponents, GET, POST, PUT }
import service.NoteService

import scala.util.{ Failure, Success }

class NoteControllerSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val noteService = mock[NoteService]

    val noteController = new NoteController(stubControllerComponents())
    // noteServiceフィールドをmock
    Whitebox.setInternalState(noteController, "noteService", noteService)

    val noteIdDto1 = 1
    val noteId1 = NoteId(noteIdDto1)
    val userId1 = UserId(1)
    val title1 = Title("title1")
    val content1 = NoteContent("content1")
    val status1 = Active
    val note1 = Note(noteId1, userId1, title1, content1, status1, None)
    val newNote1 = NewNote(userId1, title1, content1, None)

    val noteIdDto2 = 2
    val noteId2 = NoteId(noteIdDto2)
    val userId2 = UserId(2)
    val title2 = Title("title2")
    val content2 = NoteContent("content2")
    val parentFolderId2 = FolderId(1)
    val newNote2 = NewNote(userId2, title2, content2, Some(parentFolderId2))

    case class TitleAndContent(
        title: Title,
        content: NoteContent
    )
    val titleAndContent1 = TitleAndContent(title1, content1)

    def newNoteDto(newNote: NewNote): NewNoteDto = NewNoteDto(
      newNote.userId.value,
      newNote.title.value,
      newNote.content.value,
      newNote.parentFolderId.map(_.value)
    )

    implicit val newNoteWrites: Writes[NewNoteDto] = (
      (JsPath \ "user_id").write[Int] and
        (JsPath \ "title").write[String] and
        (JsPath \ "content").write[String] and
        (JsPath \ "parent_folder_id").writeNullable[Int]
    )(unlift(NewNoteDto.unapply))

    implicit val titleAndContentWrites = new Writes[TitleAndContent] {
      def writes(titleAndContent: TitleAndContent): JsObject = Json.obj(
        "title" -> titleAndContent.title.value,
        "content" -> titleAndContent.content.value
      )
    }
  }

  "#show" should {
    "return Json phased Note" in new Context {
      noteService.findBy(noteId1) returns Success(note1)

      val home = noteController.show(noteIdDto1).apply(FakeRequest(GET, s"/user/$noteIdDto1"))

      status(home) mustBe OK
      contentAsString(home) mustBe "{\"id\":1,\"user_id\":1,\"title\":\"title1\",\"content\":\"content1\",\"status\":\"active\"}"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      noteService.findBy(noteId1) returns Failure(exception)

      val home = noteController.show(noteIdDto1).apply(FakeRequest(GET, s"/user/$noteIdDto1"))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#save" should {

    "return OK when newNote doesn't have parent_folder_id" in new Context {
      noteService.save(newNote1) returns Success(1)

      val home = noteController.save().apply(FakeRequest(POST, "/note").withJsonBody(Json.toJson(newNoteDto(newNote1))))

      status(home) mustBe OK
      contentAsString(home) mustBe "Note saved successfully"
    }

    "return OK when newNote has parent_folder_id" in new Context {
      noteService.save(newNote2) returns Success(1)

      val home = noteController.save().apply(FakeRequest(POST, "/note").withJsonBody(Json.toJson(newNoteDto(newNote2))))

      status(home) mustBe OK
      contentAsString(home) mustBe "Note saved successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      noteService.save(newNote1) returns Failure(exception)

      val home = noteController.save().apply(FakeRequest(POST, "/note").withJsonBody(Json.toJson(newNoteDto(newNote1))))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#updateTitleAndContent" should {
    "return OK" in new Context {
      noteService.updateTitleAndContent(noteId1, title1, content1) returns Success(1)

      val home = noteController
        .updateTitleAndContent(noteIdDto1)
        .apply(FakeRequest(PUT, s"/note/title-and-content/$noteIdDto1").withJsonBody(Json.toJson(titleAndContent1)))

      status(home) mustBe OK
      contentAsString(home) mustBe "Note title and content updated successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception(s"DB connection error")
      noteService.updateTitleAndContent(noteId1, title1, content1) returns Failure(exception)

      val home = noteController
        .updateTitleAndContent(noteIdDto1)
        .apply(FakeRequest(PUT, s"/note/title-and-content/$noteIdDto1").withJsonBody(Json.toJson(titleAndContent1)))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#trash" should {
    "return OK" in new Context {
      noteService.trash(noteId1) returns Success(1)

      val home = noteController.trash(noteIdDto1).apply(FakeRequest(PUT, s"/note/$noteIdDto1"))

      status(home) mustBe OK
      contentAsString(home) mustBe "Note trashed successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      noteService.trash(noteId1) returns Failure(exception)

      val home = noteController.trash(noteIdDto1).apply(FakeRequest(PUT, s"/note/$noteIdDto1"))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#activate" should {
    "return OK" in new Context {
      noteService.activate(noteId1) returns Success(1)

      val home = noteController.activate(noteIdDto1).apply(FakeRequest(PUT, s"/note/trash/$noteIdDto1"))

      status(home) mustBe OK
      contentAsString(home) mustBe "Note activated successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      noteService.activate(noteId1) returns Failure(exception)

      val home = noteController.activate(noteIdDto1).apply(FakeRequest(PUT, s"/note/activate/$noteIdDto1"))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }
}
