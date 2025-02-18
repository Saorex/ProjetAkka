import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import sttp.client3._
import spray.json._

import scala.concurrent.duration._
import scala.util.Try

case object FetchMarketData
case class MarketDataUpdate(symbol: String, price: Double)
case object ShowPortfolio

// JSON Parser pour Alpha Vantage
object AlphaVantageJsonProtocol extends DefaultJsonProtocol {
  case class AlphaVantageResponse(GlobalQuote: Map[String, String])
  implicit val alphaVantageResponseFormat: RootJsonFormat[AlphaVantageResponse] = jsonFormat1(AlphaVantageResponse)
}

// Acteur gérant le portefeuille
class PortfolioActor extends Actor {
  var prices: Map[String, Double] = Map()
  
  println("PortfolioActor créé !")

  def receive: Receive = {
    case MarketDataUpdate(symbol, price) =>
      prices += (symbol -> price)
      println(s"Mise à jour du prix : $symbol = $price dollars")

    case ShowPortfolio =>
      println("Affichage du portefeuille actuel...")
      if (prices.isEmpty) {
        println("Le portefeuille est vide.")
      } else {
        prices.foreach { case (symbol, price) =>
          println(s"$symbol : $price dollars")
        }
      }
  }
}

object PortfolioActor {
  def props(): Props = Props[PortfolioActor]
}