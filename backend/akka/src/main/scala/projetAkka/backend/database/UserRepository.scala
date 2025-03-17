package projetAkka.backend.database

import projetAkka.backend.actors.AuthActor
import projetAkka.backend.models.User
import slick.jdbc.PostgresProfile.api._
import org.mindrot.jbcrypt.BCrypt
import scala.concurrent.{ExecutionContext, Future}
import org.slf4j.LoggerFactory

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def email = column[String]("email")
  def passwordHash = column[String]("password_hash")
  def walletKey = column[String]("wallet_key")

  def * = (id, username, email, passwordHash, walletKey) <> ((User.apply _).tupled, User.unapply)
}

class UserRepository(db: Database)(implicit ec: ExecutionContext) {

  private val users = TableQuery[UserTable]
  private val log = LoggerFactory.getLogger(getClass)  

  
  def validateUser(username: String, password: String): Future[Either[String, String]] = {
    log.info(s"Vérification des identifiants pour l'utilisateur : $username")

    db.run(users.filter(_.username === username).result.headOption).map {
      case Some(user) =>
        val isPasswordValid = BCrypt.checkpw(password, user.passwordHash)
        if (isPasswordValid) {
          Right("TOKEN_EXEMPLE") 
        } else {
          Left("Mot de passe incorrect")
        }
        
      case None =>
        Left("Utilisateur non trouvé")  
    }.recover {
      case ex: Exception =>
        log.error(s"Erreur lors de la vérification des identifiants pour l'utilisateur : $username", ex)
        Left("Erreur interne")
    }
  }

  // Méthode pour créer un utilisateur avec mot de passe haché
  def createUser(username: String, password: String, email: String, walletKey: String): Future[Either[String, Int]] = {
    log.info(s"Création de l'utilisateur : $username")

    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()) 
    val newUser = User(0, username, email, hashedPassword, walletKey)

    db.run(users returning users.map(_.id) += newUser).map { userId =>
      Right(userId)  
    }.recover {
      case ex: Exception =>
        log.error(s"Erreur lors de la création de l'utilisateur : $username", ex)
        Left("Erreur interne lors de la création de l'utilisateur")
    }
  }
}
