package global

import java.sql.{Connection, ResultSet}

import play.api.db.DBApi

import scala.util.{Failure, Success, Try}

object DBApiSupport {
	implicit class RichDBApi(dbApi: DBApi) {
		val db = dbApi.database("default")

		def selectRecords[T](sql: String, getRecoed: ResultSet => T): Seq[T] = {
			val con: Connection = db.getConnection()
			val getRecords = Try {
				val stmt = con.createStatement
				val rs = stmt.executeQuery(sql)
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
