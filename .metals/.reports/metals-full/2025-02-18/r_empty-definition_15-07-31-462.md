error id: _empty_/
file:///H:/Desktop/ING2/S2/ProjetAkka/backend/src/main/scala/projetAkka/PortefolioActor.scala
empty definition using pc, found symbol in pc: _empty_/
semanticdb not found
|empty definition using fallback
non-local guesses:
	 -

Document text:

```scala


import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import sttp.client3._
import spray.json._

import scala.concurrent.duration._
import scala.util.Try
// 📊 Acteur de gestion du portefeuille
class PortfolioActor extends Actor {
  var prices: Map[String, Double] = Map()

  println("✅ PortfolioActor créé !")

  def receive: Receive = {
    case MarketDataUpdate(symbol, price) =>
      prices += (symbol -> price)
      println(s"📌 Mise à jour : $symbol = $price USD")

    case ShowPortfolio =>
      println("📜 Affichage du portefeuille :")
      if (prices.isEmpty) println("📭 Portefeuille vide.")
      else prices.foreach { case (symbol, price) => println(s"📈 $symbol : $price USD") }
  }
}

object PortfolioActor {
  def props(): Props = Props[PortfolioActor]
}
```

#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/