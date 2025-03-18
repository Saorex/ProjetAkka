package projetAkka.backend.database

import projetAkka.backend.models.User
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def email = column[String]("email")
  def passwordHash = column[String]("password_hash")
  def walletKey = column[String]("wallet_key")

  def * = (id, username, email, passwordHash, walletKey) <> ((User.apply _).tupled, User.unapply)
}

class UserRepository()(implicit ec: ExecutionContext) {
  val db = Database.forConfig("akka.persistence.jdbc.slick.db")
  private val users = TableQuery[UserTable]

  def validateUser(username: String, password: String): Future[Boolean] = {
    db.run(users.filter(_.username === username).result.headOption).map {
      case Some(user) if user.passwordHash == password => true
      case _ => false
    }
  }
}
