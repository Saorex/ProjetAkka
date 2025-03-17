package projetAkka.backend.actors

import akka.actor.Actor
import akka.event.Logging
import projetAkka.backend.database.UserRepository
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AuthActor {
  case class Authenticate(username: String, password: String)
  case class AuthSuccess(token: String)
  case class AuthFailure(reason: String)
}

class AuthActor(userRepository: UserRepository) extends Actor {
  import AuthActor._

  val log = Logging(context.system, this)

  override def receive: Receive = {
    case Authenticate(username, password) =>
      val senderRef = sender()
      userRepository.validateUser(username, password).map {
        case true  =>
          log.info(s"Authentification réussie pour $username.")
          senderRef ! AuthSuccess("TOKEN_EXEMPLE") // Renvoie le token si l'authentification réussit
        case false =>
          log.warning(s"Authentification échouée pour $username.")
          senderRef ! AuthFailure("Identifiants incorrects") // Renvoie un échec si l'authentification échoue
      }.recover {
        case ex: Exception =>
          log.error(s"Erreur d'authentification : ${ex.getMessage}")
          senderRef ! AuthFailure("Erreur interne") // Renvoie une erreur interne en cas d'exception
      }
  }
}
