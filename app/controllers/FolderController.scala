package controllers

import domain.`object`.folder.Folder.FolderDto
import domain.`object`.folder.FoldersNotes.FoldersNotesDto
import domain.`object`.folder.NewFolder.NewFolderDto
import domain.`object`.folder.{ FolderId, FolderName, NewFolder }
import domain.`object`.note.Note.NoteDto
import domain.`object`.user.UserId
import javax.inject.Inject
import play.api.libs.functional.syntax.{ toFunctionalBuilderOps, unlift }
import play.api.libs.json._
import play.api.mvc.{ Action, AnyContent, BaseController, ControllerComponents }
import service.FolderService
import supports.{ JsonSupport, ResultSupport }

import scala.util.{ Failure, Success }

class FolderController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController
  with JsonSupport
  with ResultSupport {

  val folderService = new FolderService

  implicit val newFolderDtoReads = (
    (JsPath \ "user_id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "parent_folder_id").readNullable[Int]
  )(NewFolderDto)

  implicit val folderNameReads = reads("name", FolderName)

  implicit val folderDtoWrites: OWrites[FolderDto] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "user_id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "parent_folder_id").writeNullable[Int]
  )(unlift(FolderDto.unapply))

  implicit val noteDtoWrites: OWrites[NoteDto] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "user_id").write[Int] and
      (JsPath \ "title").write[String] and
      (JsPath \ "content").write[String] and
      (JsPath \ "status").write[String] and
      (JsPath \ "parent_folder_id").writeNullable[Int]
  )(unlift(NoteDto.unapply))

  implicit val foldersNotesDtoWrites = new Writes[FoldersNotesDto] {
    def writes(foldersNotesDto: FoldersNotesDto): JsObject = Json.obj(
      "folders" -> foldersNotesDto.folders,
      "notes" -> foldersNotesDto.notes
    )
  }

  def showAllBy(userId: Int): Action[AnyContent] =
    Action {
      val result = folderService.findFoldersNotesBy(UserId(userId)) match {
        case Success(foldersNotes) => Ok(Json.toJson(foldersNotes.dto))
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }

  def save(): Action[AnyContent] =
    Action { request =>
      val maybeNewFolderDto = request.getObject[NewFolderDto]
      val result = maybeNewFolderDto match {
        case Success(newFolderDto) =>
          val newFolder = NewFolder(newFolderDto)
          folderService.save(newFolder) match {
            case Success(_) => Ok("Folder saved successfully")
            case Failure(e) => BadRequest(e.toString)
          }
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }

  def updateName(folderId: Int): Action[AnyContent] =
    Action { request =>
      val maybeFolderName = request.getObject[FolderName]
      val result = maybeFolderName match {
        case Success(folderName) =>
          folderService.updateName(FolderId(folderId), folderName) match {
            case Success(_) => Ok("Folder name updated successfully")
            case Failure(e) => BadRequest(e.toString)
          }
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }

  def remove(folderId: Int): Action[AnyContent] =
    Action {
      val result = folderService.removeBy(FolderId(folderId)) match {
        case Success(_) => Ok("Folder removed successfully")
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }
}
