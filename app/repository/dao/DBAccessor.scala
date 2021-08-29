package repository.dao

import java.sql.{ DriverManager, ResultSet }

import domain.`object`.db.DBProperties

import scala.util.Try

class DBAccessor {
  private val driver = DBProperties.driver
  private val url = DBProperties.url
  private val username = DBProperties.username
  private val password = DBProperties.password
  Class.forName(driver)

  def selectRecords[T](sql: String, getRecoed: ResultSet => T): Try[Seq[T]] = {
    val con = DriverManager.getConnection(url, username, password)
    val readRecords = Try {
      val stmt = con.createStatement
      val rs = stmt.executeQuery(sql)
      var records = Seq.empty[T]
      while (rs.next) records = records :+ getRecoed(rs)
      records
    }
    con.close()
    readRecords
  }

  def selectRecord[T](sql: String, getRecord: ResultSet => T): Try[T] = {
    val con = DriverManager.getConnection(url, username, password)
    val readRecord = Try {
      val stmt = con.createStatement
      val rs = stmt.executeQuery(sql)
      if (rs.next) getRecord(rs)
      else throw new Exception(s"Not found record")
    }
    con.close()
    readRecord
  }

  def insertRecord(sql: String): Try[Int] = {
    val con = DriverManager.getConnection(url, username, password)
    val createRecord = Try {
      val stmt = con.createStatement
      stmt.executeUpdate(sql)
    }
    con.close()
    createRecord
  }
}
