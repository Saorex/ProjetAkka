error id: Props
file:///H:/Desktop/ING2/S2/ProjetAkka/src/main/scala/projetAkka/MarketDataActor.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
found definition using fallback; symbol Props
Document text:

```scala
import akka.actor.{Actor, ActorRef, Props}
import sttp.client3._
import spray.json._

import scala.concurrent.duration._
import scala.util.Try

// 📌 Messages pour la communication entre acteurs
case object FetchMarketData
case class MarketDataUpdate(symbol: String, price: Double)

// 📌 JSON Parser pour Yahoo Finance
object YahooJsonProtocol extends DefaultJsonProtocol {
  implicit val yahooResponseFormat = jsonFormat1(YahooResponse)
}

case class YahooResponse(regularMarketPrice: Double)

class MarketDataActor(portfolioActor: ActorRef) extends Actor {
  import context.dispatcher

  // 📌 Planification : Exécuter FetchMarketData toutes les 5 secondes
  context.system.scheduler.scheduleWithFixedDelay(0.seconds, 5.seconds, self, FetchMarketData)

  implicit val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()

  def receive: Receive = {
    case FetchMarketData =>
      val stockSymbol = "AAPL"
      val url = uri"https://query1.finance.yahoo.com/v7/finance/quote?symbols=$stockSymbol"

      val request = basicRequest.get(url).send(backend)

      request.body match {
        case Right(jsonString) =>
          extractPriceFromJson(jsonString).foreach { price =>
            portfolioActor ! MarketDataUpdate(stockSymbol, price)
          }
        case Left(error) =>
          println(s"❌ Erreur API: $error")
      }
  }

  // 📌 Fonction pour extraire le prix du JSON Yahoo Finance
  def extractPriceFromJson(json: String): Option[Double] = {
    Try {
      val jsonAst = json.parseJson
      val price = jsonAst.asJsObject.fields("quoteResponse").asJsObject
        .fields("result").asInstanceOf[JsArray].elements.head.asJsObject
        .fields("regularMarketPrice").convertTo[Double]
      price
    }.toOption
  }
}

// 📌 Factory pour créer l’acteur
object MarketDataActor {
  def props(portfolioActor: ActorRef): Props = Props(new MarketDataActor(portfolioActor))
}

```

#### Short summary: 

empty definition using pc, found symbol in pc: 