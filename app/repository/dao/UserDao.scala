package repository.dao

import java.sql.ResultSet

import domain.`object`.db.Sql
import global.DBSupport.RichSql

import scala.util.Try

class UserDao() {

  case class UserDto(
      id: Int,
      name: String
  )

  case class NewUserDto(
      name: String
  )

  private val userDto = (rs: ResultSet) => {
    val userId = rs.getInt("user_id")
    val userName = rs.getString("user_name")
    UserDto(userId, userName)
  }

  def selectAll(): Try[Seq[UserDto]] = {
    val sql = Sql("select * from user")
    sql.selectRecords(userDto)
  }

  def selectBy(userId: Int): Try[UserDto] = {
    val sql = Sql(s"select * from user where user_id = $userId")
    sql.selectRecord(userDto)
  }

  def insert(newUserDto: NewUserDto): Try[Int] = {
    val sql = Sql(s"insert into user (user_name) values (${newUserDto.name})")
    sql.insertRecord()
  }
}
