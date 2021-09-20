package repository.dao

import java.sql.{ DriverManager, ResultSet, Statement }

import domain.`object`.db.DBProperties

import scala.util.Try

object DBAccessor {

  private val driver = DBProperties.driver
  private val url = DBProperties.url
  private val username = DBProperties.username
  private val password = DBProperties.password

  Class.forName(driver)

  // 複数のレコードを取得
  def selectRecords[T](sql: String, getRecoed: ResultSet => T): Try[Seq[T]] = {
    val readRecords = (stmt: Statement) => {
      val rs = stmt.executeQuery(sql)
      var records = Seq.empty[T]
      while (rs.next) records = records :+ getRecoed(rs)
      records
    }
    connect(readRecords)
  }

  // 1つのレコードを取得
  def selectRecord[T](sql: String, getRecord: ResultSet => T): Try[T] = {
    val readRecord = (stmt: Statement) => {
      val rs = stmt.executeQuery(sql)
      if (rs.next) getRecord(rs)
      else throw new Exception(s"Not found record")
    }
    connect(readRecord)
  }

  // 実行&最後にinsertしたIDを返す
  def executeAndGetId(sql: String): Try[Int] = {
    val executeSql = (stmt: Statement) => {
      stmt.executeUpdate(sql)
      val rs = stmt.executeQuery("select LAST_INSERT_ID()")
      if (rs.next) rs.getInt("LAST_INSERT_ID()")
      else throw new Exception(s"Not found LAST_INSERT_ID()")
    }
    connect(executeSql)
  }

  // insert, update, deleteの実行
  def execute(sql: String): Try[Int] = {
    val executeSql = (stmt: Statement) => stmt.executeUpdate(sql)
    connect(executeSql)
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
