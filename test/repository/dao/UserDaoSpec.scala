package repository.dao

import java.sql.ResultSet

import domain.`object`.user.NewUser.NewUserDto
import domain.`object`.user.User.UserDto
import org.scalatestplus.play.PlaySpec
import testSupport.DBSupport

class UserDaoSpec extends PlaySpec {

  trait Context {
    val userDao = new UserDao
    val tableName = "user"

    val userIdDto1 = 1
    val userName1 = "taro"
    val email1 = "taro@xxx.com"
    val newUserDto1 = NewUserDto(userName1, email1)

    val userName2 = "jiro"
    val email2 = "jiro@xxx.com"
    val newUserDto2 = NewUserDto(userName2, email2)

    val userDto = (rs: ResultSet) => {
      val id = rs.getInt("id")
      val name = rs.getString("name")
      val email = rs.getString("email")
      UserDto(id, name, email)
    }

    val selectAllSql = s"select * from $tableName"
    val insertSql = (newUserDto: NewUserDto) =>
      s"insert into $tableName (name, email) values ('${newUserDto.name}', '${newUserDto.email}')"
  }

  "#selectAll" should {
    "return all user records" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newUserDto1))
          DBAccessor.execute(insertSql(newUserDto2))

          val userDtos = userDao.selectAll().get

          userDtos.length mustBe 2
          userDtos(0).name mustBe userName1
          userDtos(0).email mustBe email1
          userDtos(1).name mustBe userName2
          userDtos(1).email mustBe email2
        }
      )
    }
  }

  "#selectBy(userId: Int)" should {
    "return a user record associated with userId" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newUserDto1))

          val userDtos = DBAccessor.selectRecords(selectAllSql, userDto).get
          val userId = userDtos(0).id

          val selectUser = userDao.selectBy(userId).get
          selectUser.name mustBe userName1
          selectUser.email mustBe email1
        }
      )
    }
  }

  "#selectBy(email: Email)" should {
    "return a user record associated with email" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newUserDto1))

          val userDtos = DBAccessor.selectRecords(selectAllSql, userDto).get
          val email = userDtos(0).email
          val userId = userDtos(0).id

          val selectUser = userDao.selectBy(email).get
          selectUser.id mustBe userId
          selectUser.name mustBe userName1
        }
      )
    }
  }

  "#insert" should {
    "insert user record" in new Context {
      DBSupport.dbTest(
        tableName, {
          userDao.insert(newUserDto1)

          val userDtos = DBAccessor.selectRecords(selectAllSql, userDto).get

          userDtos(0).name mustBe userName1
          userDtos(0).email mustBe email1
        }
      )
    }
  }

  "#insertAndGetId" should {
    "insert user record and return last inserted id" in new Context {
      DBSupport.dbTest(
        tableName, {
          val userId = userDao.insertAndGetId(newUserDto1).get

          val userDtos = DBAccessor.selectRecords(selectAllSql, userDto).get

          userDtos(0).id mustBe userId
          userDtos(0).name mustBe userName1
          userDtos(0).email mustBe email1
        }
      )
    }
  }

  "#updateName" should {
    "update user.name record associated with userId" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newUserDto1))
          val beforeUserDtos = DBAccessor.selectRecords(selectAllSql, userDto).get

          beforeUserDtos(0).name mustBe userName1
          val userId = beforeUserDtos(0).id

          userDao.updateName(userId, userName2)

          val updatedUserDtos = DBAccessor.selectRecords(selectAllSql, userDto).get
          updatedUserDtos(0).name mustBe userName2
        }
      )
    }
  }

  "#deleteBy" should {
    "delete user record associated with userId" in new Context {
      DBSupport.dbTest(
        tableName, {
          DBAccessor.execute(insertSql(newUserDto1))
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
