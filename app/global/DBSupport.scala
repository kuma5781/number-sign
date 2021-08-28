package global

import java.sql.{ DriverManager, ResultSet }

import domain.`object`.db.{ DBProperties, Sql }

import scala.util.{ Failure, Success, Try }

object DBSupport {
  implicit class RichSql(sql: Sql) {

    val driver = DBProperties.driver
    val url = DBProperties.url
    val username = DBProperties.username
    val password = DBProperties.password
    Class.forName(driver)

    def selectRecords[T](getRecoed: ResultSet => T): Seq[T] = {
      val con = DriverManager.getConnection(url, username, password)
      val getRecords = Try {
        val stmt = con.createStatement
        val rs = stmt.executeQuery(sql.value)
        var records = Seq.empty[T]
        while (rs.next) records = records :+ getRecoed(rs)
        records
      }
      con.close()
      getRecords match {
        case Success(records) => records
        case Failure(e) => throw new Exception(e.getMessage, e)
      }
    }
  }
}
