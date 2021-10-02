package domain.`object`.user

case class UserName(value: String)

object UserName{
  def generateFrom(email: Email): UserName ={
    val userName = email.value.split('@').toList.head
    UserName(userName)
  }
}
