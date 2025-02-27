package projetAkka

import akka.actor.{ActorSystem, Props}
import projetAkka.backend.actors._
import io.github.cdimascio.dotenv.Dotenv

object Main extends App {

  // Initialisation de l'Actor System
  val system = ActorSystem("InvestmentSystem")

  // Création des acteurs
  val userActor = system.actorOf(Props[UserActor], "userActor")
  val marketActor = system.actorOf(Props[MarketDataActor], "marketActor")

  // Envoi de messages aux acteurs
  userActor ! CreatePortfolio("User1")
  userActor ! AddStockToPortfolio("AAPL", 10)
  userActor ! AddStockToPortfolio("GOOGL", 5)
  userActor ! ShowPortfolio

  marketActor ! FetchMarketData

  // Attente avant l'arrêt du système
  Thread.sleep(2000)
  system.terminate()
}
