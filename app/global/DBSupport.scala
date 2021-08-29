package global

import java.sql.{DriverManager, ResultSet}

import domain.`object`.db.{DBProperties, Sql}

import scala.util.Try

object DBSupport {
  implicit class RichSql(sql: Sql) {

    private val driver = DBProperties.driver
    private val url = DBProperties.url
    private val username = DBProperties.username
    private val password = DBProperties.password
    Class.forName(driver)

    def selectRecords[T](getRecoed: ResultSet => T): Try[Seq[T]] = {
      val con = DriverManager.getConnection(url, username, password)
      val readRecords = Try {
        val stmt = con.createStatement
        val rs = stmt.executeQuery(sql.value)
        var records = Seq.empty[T]
        while (rs.next) records = records :+ getRecoed(rs)
        records
      }
      con.close()
      readRecords
    }

    def selectRecord[T](getRecord: ResultSet => T): Try[T] = {
      val con = DriverManager.getConnection(url, username, password)
      val readRecord = Try {
        val stmt = con.createStatement
        val rs = stmt.executeQuery(sql.value)
        if (rs.next) getRecord(rs)
        else throw new Exception(s"Not found record")
      }
      con.close()
      readRecord
    }

    def insertRecord(): Try[Int] = {
      val con = DriverManager.getConnection(url, username, password)
      val createRecord = Try {
        val stmt = con.createStatement
        stmt.executeUpdate(sql.value)
      }
      con.close()
      createRecord
    }
  }
}
