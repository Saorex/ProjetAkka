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
  val symbol = "BTCUSDT"
  val interval = "1m"
  val limit = 1
  val apiUrl = s"https://api.binance.com/api/v3/klines?symbol=$symbol&interval=$interval&limit=$limit"
  val dataFetcher = system.actorOf(Props(new DataFetcherActor(apiUrl,symbol)), "dataFetcher")



  // Création des acteurs
  val marketActor = system.actorOf(Props[MarketDataActor], "marketDataActor")
  val marketManager = system.actorOf(Props(new MarketManagerActor(marketActor)), "marketManager")
  val user = system.actorOf(Props[UserActor], "userActor")

  // Envoi de messages aux acteurs
  user ! CreatePortfolio("user1")
  user ! AddStockToPortfolio("AAPL", 10)
  user ! AddStockToPortfolio("TSLA", 5)
  user ! ShowPortfolio

  marketManager ! StartFetching

  // Attente pour maintenir le serveur en vie
  println("Press ENTER to stop the server...")
  StdIn.readLine()
  system.terminate()
}
