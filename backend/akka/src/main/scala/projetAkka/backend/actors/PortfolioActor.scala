package projetAkka.backend.actors

import akka.actor.Actor

// Messages pour l'acteur Portfolio
case class AddStock(symbol: String, quantity: Int)
case class RemoveStock(symbol: String)
case object GetPortfolio

class PortfolioActor extends Actor {
  var portfolio = Map[String, Int]() // Stocke les actions et quantités

  def receive = {
    case AddStock(symbol, quantity) =>
      portfolio = portfolio + (symbol -> (portfolio.getOrElse(symbol, 0) + quantity))
      println(s"Ajouté $quantity de $symbol au portefeuille.")

    case RemoveStock(symbol) =>
      portfolio = portfolio - symbol
      println(s"Retiré $symbol du portefeuille.")

    case GetPortfolio =>
      println(s"Portefeuille actuel : $portfolio")
  }
}
