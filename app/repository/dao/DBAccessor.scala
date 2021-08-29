package repository.dao

import java.sql.{ ResultSet, Statement }

import play.api.db.DBApi

import scala.util.Try

object DBAccessor {

  implicit class RichDBApi(dbApi: DBApi) {
    val db = dbApi.database("default")

    def selectRecords[T](sql: String, getRecoed: ResultSet => T): Try[Seq[T]] = {
      val readRecords = (stmt: Statement) => {
        val rs = stmt.executeQuery(sql)
        var records = Seq.empty[T]
        while (rs.next) records = records :+ getRecoed(rs)
        records
      }
      connection(readRecords)
    }

    def selectRecord[T](sql: String, getRecord: ResultSet => T): Try[T] = {
      val readRecord = (stmt: Statement) => {
        val rs = stmt.executeQuery(sql)
        if (rs.next) getRecord(rs)
        else throw new Exception(s"Not found record")
      }
      connection(readRecord)
    }

    def insertRecord(sql: String): Try[Int] = {
      val createRecord = (stmt: Statement) => stmt.executeUpdate(sql)
      connection(createRecord)
    }

    private def connection[T](fun: Statement => T): Try[T] = {
      val con = db.getConnection()
      val createRecord = Try {
        val stmt = con.createStatement
        fun(stmt)
      }
      con.close()
      createRecord
    }
  }
}
