package projetAkka

import akka.actor.{ActorSystem, Props}
import projetAkka.backend.actors._
import projetAkka.backend.routes._
import akka.http.scaladsl.Http
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App {

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

  // Création des acteurs
  val userActor = system.actorOf(Props[UserActor], "userActor")
  val marketActor = system.actorOf(Props[MarketDataActor], "marketActor")

  // Envoi de messages aux acteurs
  userActor ! CreatePortfolio("User1")
  userActor ! AddStockToPortfolio("AAPL", 10)
  userActor ! AddStockToPortfolio("GOOGL", 5)
  userActor ! ShowPortfolio

  marketActor ! FetchMarketData

  Await.result(server, Duration.Inf)
}
