package controllers

import domain.`object`.folder.NewFolder.NewFolderDto
import domain.`object`.folder.{ FolderId, FolderName, NewFolder }
import domain.`object`.user.UserId
import org.powermock.reflect.Whitebox
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import play.api.http.Status.{ BAD_REQUEST, OK }
import play.api.libs.functional.syntax.{ toFunctionalBuilderOps, unlift }
import play.api.libs.json.{ JsPath, Json, Writes }
import play.api.test.FakeRequest
import play.api.test.Helpers.{ contentAsString, defaultAwaitTimeout, status, stubControllerComponents, POST }
import service.FolderService

import scala.util.{ Failure, Success }

class FolderControllerSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val folderService = mock[FolderService]

    val folderController = new FolderController(stubControllerComponents())
    // folderServiceフィールドをmock
    Whitebox.setInternalState(folderController, "folderService", folderService)

    val userId1 = UserId(1)
    val folderName1 = FolderName("name")
    val newFolder1 = NewFolder(userId1, folderName1, None)

    val userId2 = UserId(2)
    val folderName2 = FolderName("name2")
    val parentFolderId2 = FolderId(1)
    val newFolder2 = NewFolder(userId2, folderName2, Some(parentFolderId2))

    def newFolderDto(newFolder: NewFolder): NewFolderDto = NewFolderDto(
      newFolder.userId.value,
      newFolder.name.value,
      newFolder.parentFolderId.map(_.value)
    )

    implicit val newNoteWrites: Writes[NewFolderDto] = (
      (JsPath \ "user_id").write[Int] and
        (JsPath \ "name").write[String] and
        (JsPath \ "parent_folder_id").writeNullable[Int]
    )(unlift(NewFolderDto.unapply))
  }

  "#save" should {
    "return OK when newFolder has parent_folder_id" in new Context {
      folderService.save(newFolder1) returns Success(1)

      val home =
        folderController.save().apply(FakeRequest(POST, "/folder").withJsonBody(Json.toJson(newFolderDto(newFolder1))))

      status(home) mustBe OK
      contentAsString(home) mustBe "Folder saved successfully"
    }

    "return OK when newFolder doesn't have parent_folder_id" in new Context {
      folderService.save(newFolder2) returns Success(1)

      val home =
        folderController.save().apply(FakeRequest(POST, "/folder").withJsonBody(Json.toJson(newFolderDto(newFolder2))))

      status(home) mustBe OK
      contentAsString(home) mustBe "Folder saved successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception(s"DB connection error")
      folderService.save(newFolder1) returns Failure(exception)

      val home =
        folderController.save().apply(FakeRequest(POST, "/folder").withJsonBody(Json.toJson(newFolderDto(newFolder1))))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }
}
