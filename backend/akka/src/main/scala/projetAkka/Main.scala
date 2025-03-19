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

  // Initialisation des routes
  val routes = new Routes(simulationActor)

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
  val userActor = system.actorOf(Props[UserActor], "userActor")
  val marketActor = system.actorOf(Props[MarketDataActor], "marketDataActor")
  val simulationActor = system.actorOf(Props[SimulationActor], "simulationActor")
  val marketManager = system.actorOf(Props(new MarketManagerActor(marketActor)), "marketManager")

  // Envoi de messages aux acteurs
  userActor ! CreatePortfolio("user1")
  userActor ! AddStockToPortfolio("AAPL", 10)
  userActor ! AddStockToPortfolio("TSLA", 5)
  userActor ! ShowPortfolio

  // Authentification avec UserRepository
  val userRepository = new UserRepository()
  val authActor = system.actorOf(Props(new AuthActor(userRepository)), "authActor")

  // Démarrage du serveur HTTP Akka 
  val server = try {
    Http().newServerAt("localhost", 8080).bind(allRoutes)
  } catch {
    case ex: Exception =>
      println(s"Erreur lors du démarrage du serveur: ${ex.getMessage}")
      system.terminate()
      throw ex
  }

  server.map { _ =>
    println("Serveur démarré sur http://localhost:8080")
  } recover {
    case ex =>
      println(s"Erreur au démarrage du serveur: ${ex.getMessage}")
      system.terminate()
  }


  simulationActor ! SimulateInvestment(10000, 10, 5, 1)

  // Démarrage du serveur HTTP
  val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes.routes)

  bindingFuture.map { binding =>
    println(s"Server now online at http://${binding.localAddress.getHostString}:${binding.localAddress.getPort}/")
  }.recover {
    case ex =>
      println(s"Failed to start the server: ${ex.getMessage}")
      system.terminate()
  }

  // Bloque le thread principal mais permet aux acteurs et au serveur de tourner
  println("Press ENTER to stop the server...")
  StdIn.readLine()

  // Arrêt propre du serveur et de l'ActorSystem
  bindingFuture.flatMap(_.unbind()).onComplete { _ =>
    system.terminate()
  }
}