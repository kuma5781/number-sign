package controllers

import java.sql

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.db.Database
import play.mvc.Controller
import play.api.db._
import play.api.mvc._
import global.ResultSupport.RichResult

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(dbApi: DBApi, val controllerComponents: ControllerComponents) extends BaseController{

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action {Ok(getFullList().toString).enableCors}
//  Request[AnyContent] =>
//    Ok(views.html.index())
//}

  def getFullList(): sql.Array = {
    val db = dbApi.database("default")
    val con = db.getConnection()
    try {
      val stmt = con.createStatement
      val rs = stmt.executeQuery("SELECT * FROM applicant")
      rs.getArray("applicant_name")
//      while (rs.next()) {
//        println(rs.getString("applicant_name"))
//      }
    } finally {
      con.close()
    }
  }

//  def masterList = Action {
//    val db = dbapi.database("default")
//    db.withConnection { implicit c =>
//      val records = SQL("SELECT * FROM cours_infos ORDER BY id")().map {
//        row =>
//          (row[Int]("id").toString,
//            Map("id" -> row[Int]("id"), "year" -> row[Int]("year"), "cours" -> row[Int]("cours")))
//      }.toMap
//
//      Ok(Json.toJson(records))
//    }
//  }
//
//  def getData = Action {
//    var outString = "Number is "
//    val conn = DB.getConnection()
//    try {
//      val stmt = conn.createStatement
//      val rs = stmt.executeQuery("SELECT 9 as testkey ")
//      while (rs.next()) {
//        outString += rs.getString("testkey")
//      }
//    } finally {
//      conn.close()
//    }
//    Ok(outString)
//  }
}