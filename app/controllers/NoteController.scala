package controllers

import domain.`object`.note.NewNote
import domain.`object`.note.NewNote.NewNoteDto
import javax.inject.{ Inject, Singleton }
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.JsPath
import play.api.mvc.{ Action, AnyContent, BaseController, ControllerComponents }
import service.NoteService
import supports.{ JsonSupport, ResultSupport }

import scala.util.{ Failure, Success }

@Singleton
class NoteController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController
  with JsonSupport
  with ResultSupport {

  val noteService = new NoteService

  implicit val newNoteDtoReads = (
    (JsPath \ "user_id").read[Int] and
      (JsPath \ "title").read[String] and
      (JsPath \ "content").read[String]
  )(NewNoteDto)

  def save(): Action[AnyContent] = {
    Action { request =>
      val maybeNewNoteDto = request.getObject[NewNoteDto]
      val result = maybeNewNoteDto match {
        case Success(newNoteDto) =>
          val newNote = NewNote(newNoteDto)
          noteService.save(newNote) match {
            case Success(_) => Ok("Note record saved successfully")
            case Failure(e) => NotFound(e.toString)
          }
        case Failure(e) => NotFound(e.toString)
      }
      result.enableCors
    }
  }
}
