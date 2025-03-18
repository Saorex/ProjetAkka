package projetAkka.backend.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import play.api.libs.json._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import projetAkka.backend.actors._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.HttpMethods._ // Importez les méthodes HTTP
import akka.http.scaladsl.model.StatusCodes._

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