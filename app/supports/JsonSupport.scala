package supports

import play.api.libs.json.{ JsDefined, JsSuccess, Reads }
import play.api.mvc.{ AnyContent, Request }

import scala.util.Try

object JsonSupport {
  implicit class RichRequest(request: Request[AnyContent]) {
    def getObject[T](implicit rds: Reads[T]): Try[T] =
      Try(request.body.asJson.get.validate[T].get)
  }

  def reads[T](vName: String, f: String => T): Reads[T] = Reads[T] { json =>
    json \ vName match {
      case JsDefined(v) => JsSuccess(f(v.as[String]))
      case _ => throw new Exception(s"Failed to read '$vName' from json")
    }
  }
}
