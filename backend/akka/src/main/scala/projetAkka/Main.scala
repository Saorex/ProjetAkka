package projetAkka.backend

import projetAkka.backend.actors._
import projetAkka.backend.routes._
import projetAkka.backend.database._

import io.github.cdimascio.dotenv.Dotenv

import akka.actor.{ActorSystem, Props}

import akka.http.scaladsl.Http
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.StdIn

import slick.jdbc.PostgresProfile.api._

object Main extends App {

  val dotenv: Dotenv = Dotenv.load()

  // Charger et définir dans les propriétés système
  dotenv.entries().forEach { entry =>
    System.setProperty(entry.getKey, entry.getValue)
  }

  // Initialisation de l'Actor System
  implicit val system: ActorSystem = ActorSystem("InvestmentSystem")

  // Démarrage du serveur HTTP
  implicit val executionContext = system.dispatcher
  val server = Http().newServerAt("localhost", 9090).bind(Routes.routes)

  server.map { _ =>
    println("Successfully started on localhost:9090")
  } recover {
    case ex =>
      println("Failed to start the server due to: " + ex.getMessage)
      system.terminate()
  }

  //Récuppération donnée
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
  val simulationActor = system.actorOf(Props[SimulationActor], "simulationActor")
  val marketActor = system.actorOf(Props[MarketDataActor], "marketDataActor")
  val marketManager = system.actorOf(Props(new MarketManagerActor(marketActor)), "marketManager")

  // Envoi de messages aux acteurs
  user ! CreatePortfolio("user1")
  user ! AddStockToPortfolio("AAPL", 10)
  user ! AddStockToPortfolio("TSLA", 5)
  user ! ShowPortfolio

  marketManager ! StartFetching

  // Exemple de simulation
  simulationActor ! SimulateInvestment(10000, 10, 5, 1)
  
  // Attente pour maintenir le serveur en vie
  println("Press ENTER to stop the server...")
  StdIn.readLine()
  system.terminate()
}
