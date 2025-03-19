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
        case Right(success) =>
          log.info(s"Authentification réussie pour $username.")
          senderRef ! success 
        case Left(failure) =>
          log.warning(s"Authentification échouée pour $username.")
          senderRef ! failure 
      }.recover {
        case ex: Exception =>
          log.error(s"Erreur d'authentification : ${ex.getMessage}")
          senderRef ! AuthFailure("Erreur interne") 
      }
  }
}
