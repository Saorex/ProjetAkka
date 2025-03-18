package projetAkka.backend.routes

import projetAkka.backend.actors._

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import play.api.libs.json._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.HttpMethods._ // Importez les méthodes HTTP
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.headers._

import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

case class LoginRequest(username: String, password: String)
case class LoginResponse(token: String)

//trait JsonSupport extends DefaultJsonProtocol {
//  implicit val loginRequestFormat: RootJsonFormat[LoginRequest] = jsonFormat2(LoginRequest)
//  implicit val loginResponseFormat: RootJsonFormat[LoginResponse] = jsonFormat1(LoginResponse)
//}


/*class AuthRoutes(authActor: ActorRef)(implicit ec: ExecutionContext) extends JsonSupport {

  implicit val timeout: Timeout = Timeout(10.seconds)

  // Définition de la route CORS pour l'authentification
  private def corsRoute = cors() {
    path("login") {
      post {
        entity(as[LoginRequest]) { loginRequest =>
          val responseFuture = (authActor ? Authenticate(loginRequest.username, loginRequest.password))
            .mapTo[Either[String, String]]

          onComplete(responseFuture) {
            case scala.util.Success(Right(token)) => complete(LoginResponse(token))  // Succès
            case scala.util.Success(Left(errorMessage)) => complete(401, errorMessage)  // Erreur
            case scala.util.Failure(ex) => complete(500, s"Erreur du serveur: ${ex.getMessage}")  // Erreur serveur
          }
        }
      }
    }
  }

  val route: Route = corsRoute
}*/


//object Routes {
  
//  def routes(authActor: ActorRef)(implicit ec: ExecutionContext): Route =
//    path("hello") {
//      get {
//        complete("Hello World")
//      }
//    } ~ new AuthRoutes(authActor).route  
//}


class Routes(simulationActor: ActorRef)(implicit system: ActorSystem, executionContext: ExecutionContext, timeout: Timeout) {

  implicit val simulateInvestmentFormat: Format[SimulateInvestment] = Json.format[SimulateInvestment]
  implicit val simulationResultFormat: Format[SimulationResult] = Json.format[SimulationResult]

  val corsHandler = {
    respondWithHeaders(
      `Access-Control-Allow-Origin`.*,
      `Access-Control-Allow-Credentials`(true),
      `Access-Control-Allow-Headers`("Content-Type", "X-Requested-With"),
      `Access-Control-Allow-Methods`(OPTIONS, POST)
    ) {
      options {
        complete(OK)
      } ~ route
    }
  }

  val route: Route =
    path("simulations") {
      post {
        entity(as[JsValue]) { json =>
          json.validate[SimulateInvestment] match {
            case JsSuccess(simulateInvestment, _) =>
              onSuccess((simulationActor ? simulateInvestment).mapTo[SimulationResult]) {
                case SimulationResult(data) => complete(Json.toJson(data))
              }
            case JsError(errors) =>
              complete(BadRequest -> s"Invalid JSON: ${errors.mkString(", ")}")
          }
        }
      }
    }

  val routes: Route = corsHandler
}
