package controllers

import domain.`object`.note.{ NewNote, NoteContent, Title }
import domain.`object`.user.UserId
import org.powermock.reflect.Whitebox
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import play.api.http.Status.{ BAD_REQUEST, OK }
import play.api.libs.json.{ JsObject, Json, Writes }
import play.api.test.FakeRequest
import play.api.test.Helpers.{ contentAsString, defaultAwaitTimeout, status, stubControllerComponents, POST }
import service.NoteService

import scala.util.{ Failure, Success }

class NoteControllerSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val noteService = mock[NoteService]

    val noteController = new NoteController(stubControllerComponents())
    // noteServiceフィールドをmock
    Whitebox.setInternalState(noteController, "noteService", noteService)

    val userId = UserId(1)
    val title = Title("title")
    val content = NoteContent("content")
    val newNote = NewNote(userId, title, content)

    implicit val newNoteWrites = new Writes[NewNote] {
      def writes(newNote: NewNote): JsObject = Json.obj(
        "user_id" -> newNote.userId.value,
        "title" -> newNote.title.value,
        "content" -> newNote.content.value
      )
    }
  }

  "#save" should {
    "return OK" in new Context {
      noteService.save(newNote) returns Success(1)

      val home = noteController.save().apply(FakeRequest(POST, "/note").withJsonBody(Json.toJson(newNote)))

      status(home) mustBe OK
      contentAsString(home) mustBe "Note record saved successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception(s"DB connection error")
      noteService.save(newNote) returns Failure(exception)

      val home = noteController.save().apply(FakeRequest(POST, "/note").withJsonBody(Json.toJson(newNote)))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }
}
