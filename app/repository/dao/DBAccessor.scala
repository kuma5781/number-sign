package repository.dao

import java.sql.{ DriverManager, ResultSet, Statement }

import domain.`object`.db.DBProperties

import scala.util.Try

object DBAccessor {

  val driver = DBProperties.driver
  val url = DBProperties.url
  val username = DBProperties.username
  val password = DBProperties.password

  Class.forName(driver)

  def selectRecords[T](sql: String, getRecoed: ResultSet => T): Try[Seq[T]] = {
    val readRecords = (stmt: Statement) => {
      val rs = stmt.executeQuery(sql)
      var records = Seq.empty[T]
      while (rs.next) records = records :+ getRecoed(rs)
      records
    }
    connect(readRecords)
  }

  def selectRecord[T](sql: String, getRecord: ResultSet => T): Try[T] = {
    val readRecord = (stmt: Statement) => {
      val rs = stmt.executeQuery(sql)
      if (rs.next) getRecord(rs)
      else throw new Exception(s"Not found record")
    }
    connect(readRecord)
  }

  def insertRecord(sql: String): Try[Int] = {
    val createRecord = (stmt: Statement) => stmt.executeUpdate(sql)
    connect(createRecord)
  }

  private def connect[T](fun: Statement => T): Try[T] = {
    val con = DriverManager.getConnection(url, username, password)
    val createRecord = Try {
      val stmt = con.createStatement
      fun(stmt)
    }
    con.close()
    createRecord
  }
}
