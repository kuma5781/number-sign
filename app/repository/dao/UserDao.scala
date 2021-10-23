package repository.dao

import java.sql.ResultSet
import domain.`object`.user.NewUser.NewUserDto
import domain.`object`.user.User.UserDto

import scala.util.Try

class UserDao {

  private val tableName = "user"

  private val userDto = (rs: ResultSet) => {
    val id = rs.getInt("id")
    val name = rs.getString("name")
    val email = rs.getString("email")
    UserDto(id, name, email)
  }

  def selectAll(): Try[Seq[UserDto]] = {
    val sql = s"select * from $tableName"
    DBAccessor.selectRecords(sql, userDto)
  }

  def selectBy(userId: Int): Try[UserDto] = {
    val sql = s"select * from $tableName where id = $userId"
    DBAccessor.selectRecord(sql, userDto)
  }

  def selectBy(email: String): Try[UserDto] = {
    val sql = s"select * from $tableName where email = '$email'"
    DBAccessor.selectRecord(sql, userDto)
  }

  def insert(newUserDto: NewUserDto): Try[Int] = {
    val sql = s"insert into $tableName (name, email) values ('${newUserDto.name}', '${newUserDto.email}')"
    DBAccessor.execute(sql)
  }

  def insertAndSelect(newUserDto: NewUserDto): Try[UserDto] = {
    val sqlInsert = s"insert into $tableName (name, email) values ('${newUserDto.name}', '${newUserDto.email}')"
    val sqlSelect = (userId: Int) => s"select * from $tableName where id = $userId"
    DBAccessor.executeAndSelectRecord(sqlInsert, sqlSelect, userDto)
  }

  def updateName(userId: Int, userName: String): Try[Int] = {
    val sql = s"update $tableName set name = '$userName' where id = $userId"
    DBAccessor.execute(sql)
  }

  def deleteBy(userId: Int): Try[Int] = {
    val sql = s"delete from $tableName where id = $userId"
    DBAccessor.execute(sql)
  }
}
