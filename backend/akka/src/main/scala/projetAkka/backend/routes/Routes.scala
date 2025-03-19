package projetAkka.backend.routes

import projetAkka.backend.actors._
import projetAkka.backend.actors.AuthActor._
import projetAkka.backend.database._

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import play.api.libs.json._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent.Future

import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.HttpMethods._ // Importez les méthodes HTTP
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.headers._

import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

case class LoginRequest(username: String, password: String)
case class LoginResponse(token: String)

class AuthRoutes(authActor: ActorRef)(implicit ec: ExecutionContext, timeout: Timeout) {
  case class LoginRequest(username: String, password: String)
  case class LoginResponse(token: String)

  implicit val loginRequestFormat: Format[LoginRequest] = Json.format[LoginRequest]
  implicit val loginResponseFormat: Format[LoginResponse] = Json.format[LoginResponse]

  val route: Route = cors() {
    pathPrefix("api") {
      path("login") {
        post {
          entity(as[LoginRequest]) { loginRequest =>
            val responseFuture = (authActor ? Authenticate(loginRequest.username, loginRequest.password))
              .mapTo[Either[String, String]]

            onComplete(responseFuture) {
              case scala.util.Success(Right(token)) => complete(LoginResponse(token))
              case scala.util.Success(Left(errorMessage)) => complete(Unauthorized, errorMessage)
              case scala.util.Failure(ex) => complete(InternalServerError, s"Erreur du serveur: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }
}

class SimulationRoutes(simulationActor: ActorRef)(implicit system: ActorSystem, executionContext: ExecutionContext, timeout: Timeout) {
  implicit val simulateInvestmentFormat: Format[SimulateInvestment] = Json.format[SimulateInvestment]
  implicit val simulationResultFormat: Format[SimulationResult] = Json.format[SimulationResult]

  val route: Route = cors() {
    pathPrefix("api") {
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
    }
  }
}

class MarketDataRoutes()(implicit system: ActorSystem, executionContext: ExecutionContext, timeout: Timeout) {

  implicit val marketDataFormat: Format[MarketData] = Json.format[MarketData]

  val route: Route = cors() {
    pathPrefix("api") {
      path("data") {
        get {
          parameters("symbol") { symbol =>
            val dataFuture: Future[Seq[MarketData]] = MarketDataRepository.getMarketDataBySymbol(symbol)

            onComplete(dataFuture) {
              case scala.util.Success(data) =>
                if (data.nonEmpty) complete(Json.toJson(data))
                else complete(NotFound, s"Aucune donnée trouvée pour le symbole : $symbol")
              case scala.util.Failure(ex) =>
                complete(InternalServerError, s"Erreur du serveur : ${ex.getMessage}")
            }
          }
        }
      }
    }
  }
}

class ApiRoutes(authActor: ActorRef, simulationActor: ActorRef)(implicit system: ActorSystem, executionContext: ExecutionContext, timeout: Timeout) {
  private val authRoutes = new AuthRoutes(authActor).route
  private val simulationRoutes = new SimulationRoutes(simulationActor).route
  private val marketDataRoutes = new MarketDataRoutes().route

  val routes: Route = authRoutes ~ simulationRoutes ~ marketDataRoutes
}
