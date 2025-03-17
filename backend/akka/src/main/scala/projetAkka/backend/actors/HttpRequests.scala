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
import io.github.cdimascio.dotenv.Dotenv

import scala.io.StdIn
import projetAkka.backend.actors.{SimulateInvestment, SimulationResult, SimulationActor}
import projetAkka.backend.routes.Routes

object WebServer {
  val dotenv: Dotenv = Dotenv.load()

  // Charger et définir dans les propriétés système
  dotenv.entries().forEach { entry =>
    System.setProperty(entry.getKey, entry.getValue)
  }

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("InvestmentSystem")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    implicit val timeout: Timeout = Timeout(5.seconds)

    // Création de l'acteur SimulationActor
    val simulationActor = system.actorOf(Props[SimulationActor], "simulationActor")

    // Initialisation des routes
    val routes = new Routes(simulationActor)

    // Démarrage du serveur HTTP
    val bindingFuture = Http().bindAndHandle(routes.routes, "localhost", 3000)
    println(s"Server now online. Please navigate to http://localhost:3000/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
