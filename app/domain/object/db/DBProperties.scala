package domain.`object`.db

object DBProperties {
  val driver = sys.env("MYSQL_DRIVER")
  val url = sys.env("MYSQL_URL")
  val username = sys.env("MYSQL_USERNAME")
  val password = sys.env("MYSQL_PASSWORD")
}
