package projetAkka.backend

import akka.actor.{ActorSystem, Props}
import projetAkka.backend.actors._

object Main extends App {
 
  val system = ActorSystem("TradingSystem")

  
  val marketActor = system.actorOf(Props[MarketDataActor], "marketDataActor")
  val marketManager = system.actorOf(Props(new MarketManagerActor(marketActor)), "marketManager")
  val user = system.actorOf(Props[UserActor], "userActor")

  
  user ! CreatePortfolio("user1")
  user ! AddStockToPortfolio("AAPL", 10)
  user ! AddStockToPortfolio("TSLA", 5)
  user ! ShowPortfolio

  
  marketManager ! StartFetching

  println(" Backend Akka lancé et prêt à fonctionner.")
}
