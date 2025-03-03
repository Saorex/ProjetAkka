package projetAkka.backend.actors

import akka.actor.Actor
import scala.util.Random


case object FetchMarketData

class MarketDataActor extends Actor {
  def receive = {
    case FetchMarketData =>
      val price = Random.nextDouble() * 1000 // Simule un prix aléatoire
      println(s"[Marché] Prix mis à jour : $price USD")
  }
}
