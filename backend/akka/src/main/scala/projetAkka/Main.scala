package projetAkka.backend

import projetAkka.backend.actors._
import projetAkka.backend.routes.Routes
import projetAkka.backend.database._

import io.github.cdimascio.dotenv.Dotenv
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import scala.concurrent.duration._
import scala.io.StdIn
import slick.jdbc.PostgresProfile.api._
import akka.util.Timeout

object Main extends App {

  val dotenv: Dotenv = Dotenv.load()

  // Charger et définir dans les propriétés système
  dotenv.entries().forEach { entry =>
    System.setProperty(entry.getKey, entry.getValue)
  }

  // Initialisation de l'Actor System
  implicit val system: ActorSystem = ActorSystem("InvestmentSystem")
  implicit val executionContext: scala.concurrent.ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(5.seconds)

  // Création de l'acteur SimulationActor
  val simulationActor = system.actorOf(Props[SimulationActor], "simulationActor")

  // Initialisation des routes
  val routes = new Routes(simulationActor)

  // Démarrage du serveur HTTP
  val server = Http().newServerAt("localhost", 9090).bind(routes.routes)

  server.map { _ =>
    println("Successfully started on localhost:9090")
  } recover {
    case ex =>
      println("Failed to start the server due to: " + ex.getMessage)
      system.terminate()
  }

  // Récupération des données
  val symbols = List(
    "BTCUSDT",
    "ETHUSDT",
    "BNBUSDT",
    "SOLUSDT",
    "AVAXUSDT",
    "INJUSDT",
    "NEARUSDT",
    "TIAUSDT",
    "PYTHUSDT",
    "WIFUSDT",
    "JUPUSDT",
  )
  val dataFetcher = system.actorOf(Props(new DataFetcherActor(symbols)), "dataFetcher")

  // Création des acteurs
  val userActor = system.actorOf(Props[UserActor], "userActor")
  val marketActor = system.actorOf(Props[MarketDataActor], "marketActor")
  val marketManager = system.actorOf(Props(new MarketManagerActor(marketActor)), "marketManager")

  // Envoi de messages aux acteurs
  userActor ! CreatePortfolio("user1")
  userActor ! AddStockToPortfolio("AAPL", 10)
  userActor ! AddStockToPortfolio("TSLA", 5)
  userActor ! ShowPortfolio

  marketManager ! StartFetching

  // Exemple de simulation
  simulationActor ! SimulateInvestment(10000, 10, 5, 1)

  // Attente pour maintenir le serveur en vie
  println("Press ENTER to stop the server...")
  StdIn.readLine()
  system.terminate()
}
