package projetAkka.backend.actors

import akka.actor.{Actor, ActorRef}
import scala.concurrent.duration._
import akka.actor.ActorSystem


case object StartFetching

class MarketManagerActor(marketDataActor: ActorRef) extends Actor {
  import context.dispatcher

  override def preStart(): Unit = {
    context.system.scheduler.scheduleAtFixedRate(0.seconds, 5.seconds, self, StartFetching)
  }

  def receive = {
    case StartFetching =>
      marketDataActor ! FetchMarketData
  }
}
