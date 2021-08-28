package repository.dao

import java.sql.ResultSet

import domain.`object`.db.Sql
import global.DBSupport.RichSql

class UserDao() {

  case class UserDto(
      id: Int,
      name: String
  )

  def selectAll(): Seq[UserDto] = {
    val sql = Sql("select * from user")
    val userDto = (rs: ResultSet) => {
      val userId = rs.getInt("user_id")
      val userName = rs.getString("user_name")
      UserDto(userId, userName)
    }
    sql.selectRecords(userDto)
  }
}
