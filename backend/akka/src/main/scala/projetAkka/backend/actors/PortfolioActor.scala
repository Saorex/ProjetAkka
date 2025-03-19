package projetAkka.backend.actors

import akka.actor.Actor

case class AddStock(symbol: String, quantity: Int)
case class RemoveStock(symbol: String)
case object GetPortfolio

class PortfolioActor extends Actor {
  var portfolio = Map[String, Int]()

  def receive = {
    case AddStock(symbol, quantity) =>
      portfolio = portfolio + (symbol -> (portfolio.getOrElse(symbol, 0) + quantity))
      println(s"[Portefeuille] Ajouté $quantity de $symbol.")

    case RemoveStock(symbol) =>
      portfolio = portfolio - symbol
      println(s"[Portefeuille] Retiré $symbol.")

    case GetPortfolio =>
      println(s"[Portefeuille] État actuel : $portfolio")
  }
}