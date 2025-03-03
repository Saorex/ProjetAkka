package projetAkka.backend.actors

import akka.actor.{Actor, ActorRef, Props}


case class CreatePortfolio(userId: String)
case class AddStockToPortfolio(symbol: String, quantity: Int)
case class RemoveStockFromPortfolio(symbol: String)
case object ShowPortfolio

class UserActor extends Actor {
  var portfolioActor: Option[ActorRef] = None

  def receive = {
    case CreatePortfolio(userId) =>
      portfolioActor = Some(context.actorOf(Props[PortfolioActor], s"portfolio-$userId"))
      println(s"[Utilisateur] Portefeuille créé pour $userId.")

    case AddStockToPortfolio(symbol, quantity) =>
      portfolioActor.foreach(_ ! AddStock(symbol, quantity))

    case RemoveStockFromPortfolio(symbol) =>
      portfolioActor.foreach(_ ! RemoveStock(symbol))

    case ShowPortfolio =>
      portfolioActor.foreach(_ ! GetPortfolio)
  }
}
