package controllers

import domain.`object`.folder.NewFolder
import domain.`object`.folder.NewFolder.NewFolderDto
import javax.inject.Inject
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.JsPath
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

  def save(): Action[AnyContent] =
    Action { request =>
      val maybeNewFolderDto = request.getObject[NewFolderDto]
      val result = maybeNewFolderDto match {
        case Success(newFolderDto) =>
          val newFolder = NewFolder(newFolderDto)
          folderService.save(newFolder) match {
            case Success(_) => Ok("Folder record saved successfully")
            case Failure(e) => BadRequest(e.toString)
          }
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }
}
