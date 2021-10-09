package controllers

import domain.`object`.folder.NewFolder.NewFolderDto
import domain.`object`.folder.{ Folder, FolderId, FolderName, FoldersNotes, NewFolder }
import domain.`object`.note.NoteStatus.Active
import domain.`object`.note.{ Note, NoteContent, NoteId, Title }
import domain.`object`.user.UserId
import org.powermock.reflect.Whitebox
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import play.api.http.Status.{ BAD_REQUEST, OK }
import play.api.libs.functional.syntax.{ toFunctionalBuilderOps, unlift }
import play.api.libs.json.{ JsObject, JsPath, Json, Writes }
import play.api.test.FakeRequest
import play.api.test.Helpers.{
  contentAsString,
  defaultAwaitTimeout,
  status,
  stubControllerComponents,
  DELETE,
  GET,
  POST,
  PUT
}
import service.FolderService

import scala.util.{ Failure, Success }

class FolderControllerSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val folderService = mock[FolderService]

    val folderController = new FolderController(stubControllerComponents())
    // folderServiceフィールドをmock
    Whitebox.setInternalState(folderController, "folderService", folderService)

    val folderIdDto1 = 1
    val userIdDto = 1
    val folderId1 = FolderId(folderIdDto1)
    val userId = UserId(userIdDto)
    val folderName1 = FolderName("name1")
    val folder1 = Folder(folderId1, userId, folderName1, None)
    val newFolder1 = NewFolder(userId, folderName1, None)

    val folderId2 = FolderId(2)
    val folderName2 = FolderName("name2")
    val parentFolderId2 = folderId1
    val folder2 = Folder(folderId2, userId, folderName2, Some(parentFolderId2))
    val newFolder2 = NewFolder(userId, folderName2, Some(parentFolderId2))

    val noteId1 = NoteId(1)
    val title1 = Title("title1")
    val content1 = NoteContent("content1")
    val status1 = Active
    val note1 = Note(noteId1, userId, title1, content1, status1, None)

    def newFolderDto(newFolder: NewFolder): NewFolderDto = NewFolderDto(
      newFolder.userId.value,
      newFolder.name.value,
      newFolder.parentFolderId.map(_.value)
    )

    implicit val folderNameWrites = new Writes[FolderName] {
      def writes(folderName: FolderName): JsObject = Json.obj("name" -> folderName.value)
    }

    implicit val newNoteWrites: Writes[NewFolderDto] = (
      (JsPath \ "user_id").write[Int] and
        (JsPath \ "name").write[String] and
        (JsPath \ "parent_folder_id").writeNullable[Int]
    )(unlift(NewFolderDto.unapply))
  }

  "showAllBy" should {
    "return FoldersNotes associated with userId" in new Context {
      folderService.findFoldersNotesBy(userId) returns Success(FoldersNotes(Seq(folder1, folder2), Seq(note1)))

      val home = folderController.showAllBy(userIdDto).apply(FakeRequest(GET, s"/folder/$userIdDto"))

      status(home) mustBe OK
      contentAsString(home) mustBe "{\"folders\":[{\"folder_id\":1,\"user_id\":1,\"name\":\"name1\"},{\"folder_id\":2,\"user_id\":1,\"name\":\"name2\",\"parent_folder_id\":1}],\"notes\":[{\"note_id\":1,\"user_id\":1,\"title\":\"title1\",\"content\":\"content1\",\"status\":\"active\"}]}"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      folderService.findFoldersNotesBy(userId) returns Failure(exception)

      val home = folderController.showAllBy(userIdDto).apply(FakeRequest(GET, s"/folder/$userIdDto"))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
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
      val exception = new Exception("DB connection error")
      folderService.save(newFolder1) returns Failure(exception)

      val home =
        folderController.save().apply(FakeRequest(POST, "/folder").withJsonBody(Json.toJson(newFolderDto(newFolder1))))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#updateName" should {
    "return OK" in new Context {
      folderService.updateName(folderId1, folderName1) returns Success(1)

      val home = folderController
        .updateName(folderIdDto1)
        .apply(FakeRequest(PUT, s"/folder/name/$folderIdDto1").withJsonBody(Json.toJson(folderName1)))

      status(home) mustBe OK
      contentAsString(home) mustBe "Folder name updated successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception(s"DB connection error")
      folderService.updateName(folderId1, folderName1) returns Failure(exception)

      val home = folderController
        .updateName(folderIdDto1)
        .apply(FakeRequest(PUT, s"/folder/name/$folderIdDto1").withJsonBody(Json.toJson(folderName1)))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#remove" should {
    "return OK" in new Context {
      folderService.removeBy(folderId1) returns Success(1)

      val home = folderController.remove(folderIdDto1).apply(FakeRequest(DELETE, s"/folder/$folderIdDto1"))

      status(home) mustBe OK
      contentAsString(home) mustBe "Folder removed successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      folderService.removeBy(folderId1) returns Failure(exception)

      val home = folderController.remove(folderIdDto1).apply(FakeRequest(DELETE, s"/folder/$folderIdDto1"))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }
}
