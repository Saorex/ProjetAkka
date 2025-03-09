package projetAkka.backend.actors

import akka.actor.{Actor, ActorLogging}
import scala.util.Random


case object FetchMarketData

class MarketDataActor extends Actor with ActorLogging {
  def receive = {
    case FetchMarketData =>
      val price = Random.nextDouble() * 1000 // Simule un prix aléatoire
      println(s"[Marché] Prix mis à jour : $price USD")
  }
}
