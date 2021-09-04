package repository.dao

import java.sql.ResultSet

import org.scalatestplus.play.PlaySpec
import testSupport.DBSupport

class UserDaoSpec extends PlaySpec {

  trait Context {
    val userDao = new UserDao

    val tableName = "user"

    val userDto = (rs: ResultSet) => {
      val userId = rs.getInt("id")
      val userName = rs.getString("name")
      UserDto(userId, userName)
    }

    val selectAllSql = s"select * from $tableName"
    val insertSql = (userName: String) => s"insert into $tableName (name) values ('$userName')"
  }

  "#selectAll" should {
    "get all user records" in new Context {
      DBSupport.dbTest(
        tableName, {
          val userName1 = "太郎"
          val userName2 = "次郎"

          DBAccessor.execute(insertSql(userName1))
          DBAccessor.execute(insertSql(userName2))

          val userDtos = userDao.selectAll().get

          userDtos.length mustBe 2
          userDtos(0).name mustBe userName1
          userDtos(1).name mustBe userName2
        }
      )
    }
  }

  "#selectBy" should {
    "get a user record associated with userId" in new Context {
      DBSupport.dbTest(
        tableName, {
          val userName = "太郎"

          DBAccessor.execute(insertSql(userName))

          val userDtos = DBAccessor.selectRecords(selectAllSql, userDto).get
          val userId = userDtos(0).id

          userDao.selectBy(userId).get.name mustBe userName
        }
      )
    }
  }

  "#insert" should {
    "insert user record" in new Context {
      DBSupport.dbTest(
        tableName, {
          val userName = "太郎"
          val newUserDto = NewUserDto(userName)

          userDao.insert(newUserDto)

          val userDtos = DBAccessor.selectRecords(selectAllSql, userDto).get

          userDtos(0).name mustBe userName
        }
      )
    }
  }

  "#update" should {
    "update user record associated with userId" in new Context {
      DBSupport.dbTest(
        tableName, {
          val beforeUserName = "太郎"
          val updateUserName = "次郎"

          DBAccessor.execute(insertSql(beforeUserName))
          val beforeUserDtos = DBAccessor.selectRecords(selectAllSql, userDto).get

          beforeUserDtos(0).name mustBe beforeUserName
          val userId = beforeUserDtos(0).id

          val updateUserDto = UserDto(userId, updateUserName)
          userDao.update(updateUserDto)

          val updatedUserDtos = DBAccessor.selectRecords(selectAllSql, userDto).get
          updatedUserDtos(0).name mustBe updateUserName
        }
      )
    }
  }

  "#deleteBy" should {
    "delete user record associated with userId" in new Context {
      DBSupport.dbTest(
        tableName, {
          val userName = "太郎"

          DBAccessor.execute(insertSql(userName))
          val beforeUserDtos = DBAccessor.selectRecords(selectAllSql, userDto).get

          beforeUserDtos.length mustBe 1

          val userId = beforeUserDtos(0).id

          userDao.deleteBy(userId)

          val deletedUserDtos = DBAccessor.selectRecords(selectAllSql, userDto).get

          deletedUserDtos.length mustBe 0
        }
      )
    }
  }
}
