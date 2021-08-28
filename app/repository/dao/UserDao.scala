package repository.dao

import java.sql.ResultSet

import global.DBApiSupport.RichDBApi
import play.api.db.DBApi

class UserDao(dbApi: DBApi) {

  case class UserDto(
      id: Int,
      name: String
  )

  def selectAll(): Seq[UserDto] = {
    val sql = "select * from user"
    val userDto = (rs: ResultSet) => {
      val userId = rs.getInt("user_id")
      val userName = rs.getString("user_name")
      UserDto(userId, userName)
    }
    dbApi.selectRecords(sql, userDto)
  }
}
