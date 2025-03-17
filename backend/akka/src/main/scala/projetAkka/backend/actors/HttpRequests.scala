import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.stream.ActorMaterializer
import akka.pattern.ask
import akka.util.Timeout
import play.api.libs.json._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import scala.concurrent.duration._

import scala.io.StdIn
import projetAkka.backend.actors.{SimulateInvestment, SimulationResult, SimulationActor}

object WebServer {
  // Formats JSON implicites pour les messages
  implicit val simulateInvestmentFormat: Format[SimulateInvestment] = Json.format[SimulateInvestment]
  implicit val simulationResultFormat: Format[SimulationResult] = Json.format[SimulationResult]

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("InvestmentSystem")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    implicit val timeout: Timeout = Timeout(5.seconds)

    // Création de l'acteur SimulationActor
    val simulationActor = system.actorOf(Props[SimulationActor], "simulationActor")

    // Définition de la route HTTP
    val route: Route =
      path("simulations") { // Le début du chemin, à comprendre par /simulations
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

    // Démarrage du serveur HTTP
    val bindingFuture = Http().bindAndHandle(route, "localhost", 3000)
    println(s"Server now online. Please navigate to http://localhost:3000/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
