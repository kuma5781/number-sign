package global

import play.api.mvc.Result

object ResultSupport {
  implicit class RichResult(result: Result) {
    def enableCors = result.withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "OPTIONS, GET, POST, PUT, DELETE, HEAD", // OPTIONS for pre-flight
      "Access-Control-Allow-Headers" -> "Accept, Content-Type, Origin, X-Json, X-Prototype-Version, X-Requested-With", //, "X-My-NonStd-Option"
      "Access-Control-Allow-Credentials" -> "true"
    )
  }
}
