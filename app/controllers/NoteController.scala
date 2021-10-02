package controllers

import domain.`object`.note.NewNote.NewNoteDto
import domain.`object`.note.{ NewNote, NoteContent, NoteId, Title }
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
      (JsPath \ "content").read[String] and
      (JsPath \ "parent_folder_id").readNullable[Int]
  )(NewNoteDto)

  implicit val titleReads = reads("title", Title)
  implicit val contentReads = reads("content", NoteContent)

  def save(): Action[AnyContent] =
    Action { request =>
      val maybeNewNoteDto = request.getObject[NewNoteDto]
      val result = maybeNewNoteDto match {
        case Success(newNoteDto) =>
          val newNote = NewNote(newNoteDto)
          noteService.save(newNote) match {
            case Success(_) => Ok("Note saved successfully")
            case Failure(e) => BadRequest(e.toString)
          }
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }

  def updateTitleAndContent(noteId: Int): Action[AnyContent] =
    Action { request =>
      val tryResult = for {
        title <- request.getObject[Title]
        content <- request.getObject[NoteContent]
      } yield {
        noteService.updateTitleAndContent(NoteId(noteId), title, content) match {
          case Success(_) => Ok("Note title and content updated successfully")
          case Failure(e) => BadRequest(e.toString)
        }
      }
      val result = tryResult match {
        case Success(r) => r
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }

  def trash(noteId: Int): Action[AnyContent] =
    Action {
      val result = noteService.trash(NoteId(noteId)) match {
        case Success(_) => Ok("Note trashed successfully")
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }

  def activate(noteId: Int): Action[AnyContent] =
    Action {
      val result = noteService.activate(NoteId(noteId)) match {
        case Success(_) => Ok("Note activated successfully")
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }
}
