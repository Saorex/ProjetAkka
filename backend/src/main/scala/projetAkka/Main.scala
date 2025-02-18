import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import sttp.client3._
import spray.json._

import scala.concurrent.duration._
import scala.util.Try

object FinanceApp extends App {
  println("Démarrage de l'application FinanceApp...")

  val system = ActorSystem("FinanceSystem")

  // Création des acteurs
  val portfolioActor = system.actorOf(PortfolioActor.props(), "portfolio")
  val marketDataActor = system.actorOf(MarketDataActor.props(portfolioActor), "marketData")

  println("Acteurs créés : PortfolioActor et MarketDataActor")

  // Afficher le portefeuille après 10 secondes
  import system.dispatcher
  system.scheduler.scheduleOnce(10.seconds) {
    println("Affichage du portefeuille après 10 secondes...")
    portfolioActor ! ShowPortfolio
  }
}