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
        case either @ Right(_) =>
          log.info(s"Authentification réussie pour $username.")
          senderRef ! either
        case either @ Left(_) =>
          log.warning(s"Authentification échouée pour $username.")
          senderRef ! either
      }.recover {
        case ex: Exception =>
          log.error(s"Erreur d'authentification : ${ex.getMessage}")
          senderRef ! Left("Erreur interne")
      }
  }
}

