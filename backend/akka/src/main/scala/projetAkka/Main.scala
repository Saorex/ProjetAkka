package projetAkka

import akka.actor.{ActorSystem, Props}
import projetAkka.backend.actors._  

object Main extends App {

  val system = ActorSystem("InvestmentSystem")

  
  val userActor = system.actorOf(Props[UserActor], "userActor")
  val marketActor = system.actorOf(Props[MarketDataActor], "marketActor")

 
  userActor ! CreatePortfolio("User1")
  userActor ! AddStockToPortfolio("AAPL", 10)
  userActor ! AddStockToPortfolio("GOOGL", 5)
  userActor ! ShowPortfolio


  marketActor ! FetchMarketData

  
  Thread.sleep(2000)
  system.terminate()
}
