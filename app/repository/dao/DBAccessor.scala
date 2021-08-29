package repository.dao

import java.sql.ResultSet

import play.api.db.DBApi

import scala.util.Try

object DBAccessor {

  implicit class RichDBApi(dbApi: DBApi) {
    val db = dbApi.database("default")

    def selectRecords[T](sql: String, getRecoed: ResultSet => T): Try[Seq[T]] = {
      val con = db.getConnection()
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
      val con = db.getConnection()
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
      val con = db.getConnection()
      val createRecord = Try {
        val stmt = con.createStatement
        stmt.executeUpdate(sql)
      }
      con.close()
      createRecord
    }
  }
}
