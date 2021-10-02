package supports

import play.api.mvc.Result

/**
 * Resultのheader設定をしやすくするためのサポートトレイト
 */
trait ResultSupport {
  implicit class RichResult(result: Result) {
    // フロントエンドからのhttpアクセスを許可する
    def enableCors: Result = result.withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "OPTIONS, GET, POST, PUT, DELETE, HEAD", // OPTIONS for pre-flight
      "Access-Control-Allow-Headers" -> "Accept, Content-Type, Origin, X-Json, X-Prototype-Version, X-Requested-With", //, "X-My-NonStd-Option"
      "Access-Control-Allow-Credentials" -> "true"
    )
  }
}
