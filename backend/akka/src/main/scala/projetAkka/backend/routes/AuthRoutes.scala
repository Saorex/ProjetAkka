package projetAkka.backend.routes

import projetAkka.backend.actors.AuthActor
import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import projetAkka.backend.actors.AuthActor._
import akka.http.scaladsl.model.headers._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

case class LoginRequest(username: String, password: String)
case class LoginResponse(token: String)

trait JsonSupport extends DefaultJsonProtocol {
  implicit val loginRequestFormat: RootJsonFormat[LoginRequest] = jsonFormat2(LoginRequest)
  implicit val loginResponseFormat: RootJsonFormat[LoginResponse] = jsonFormat1(LoginResponse)
}

class AuthRoutes(authActor: ActorRef)(implicit ec: ExecutionContext) extends JsonSupport {

  implicit val timeout: Timeout = Timeout(10.seconds) // Augmenter le délai d'attente

  // Configuration CORS
  private def corsRoute = cors() {
    path("login") {
      post {
        entity(as[LoginRequest]) { loginRequest =>
          val responseFuture = (authActor ? Authenticate(loginRequest.username, loginRequest.password))
            .mapTo[Either[String, String]]

          onComplete(responseFuture) {
            case scala.util.Success(Right(token)) => complete(LoginResponse(token))
            case scala.util.Success(Left(errorMessage)) => complete(401, errorMessage)
            case scala.util.Failure(ex) => complete(500, s"Erreur du serveur: ${ex.getMessage}")
          }
        }
      }
    }
  }

  val route: Route = corsRoute
}
