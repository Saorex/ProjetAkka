package projetAkka.backend

import projetAkka.backend.actors._
import projetAkka.backend.routes._
import projetAkka.backend.database._
import io.github.cdimascio.dotenv.Dotenv
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import scala.concurrent.duration._
import scala.io.StdIn
import slick.jdbc.PostgresProfile.api._
import akka.util.Timeout
import java.nio.file.Paths
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

object Main extends App {

  // Chargement des variables d'environnement depuis .env
  val dotenv: Dotenv = Dotenv.load()

  // Charger et définir dans les propriétés système
  dotenv.entries().forEach { entry =>
    System.setProperty(entry.getKey, entry.getValue)
  }

  // Initialisation de l'Actor System
  implicit val system: ActorSystem = ActorSystem("InvestmentSystem")
  implicit val executionContext: scala.concurrent.ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(5.seconds)

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
    "JUPUSDT"
  )
  val dataFetcher = system.actorOf(Props(new DataFetcherActor(symbols)), "dataFetcher")

  // Création des acteurs
  val simulationActor = system.actorOf(Props[SimulationActor], "simulationActor")

  // Authentification avec UserRepository
  val userRepository = new UserRepository()
  val authActor = system.actorOf(Props(new AuthActor(userRepository)), "authActor")

  // Initialisation des routes
  val apiRoutes = new ApiRoutes(authActor, simulationActor)

  // Démarrage du serveur HTTP Akka
  val server = try {
    Http().newServerAt("localhost", 8080).bind(apiRoutes.routes)
    println("Server now online at http://locachost:8080")
  } catch {
    case ex: Exception =>
      println(s"Erreur lors du démarrage du serveur: ${ex.getMessage}")
      system.terminate()
      throw ex
  }

  simulationActor ! SimulateInvestment(10000, 10, 5, 1)

  // Bloque le thread principal mais permet aux acteurs et au serveur de tourner
  println("Press ENTER to stop the server...")
  StdIn.readLine()
}