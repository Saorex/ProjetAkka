import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import sttp.client3._
import spray.json._

import scala.concurrent.duration._
import scala.util.Try

class MarketDataActor(portfolioActor: ActorRef) extends Actor {
  import context.dispatcher
  import AlphaVantageJsonProtocol._

  println("MarketDataActor créé !")
  
  // Planification : exécuter FetchMarketData toutes les 5 secondes
  context.system.scheduler.scheduleWithFixedDelay(0.seconds, 5.seconds, self, FetchMarketData)
  println("Planification de FetchMarketData toutes les 5 secondes...")

  implicit val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()

  def receive: Receive = {
    case FetchMarketData =>
      println("FetchMarketData reçu, récupération du prix en cours...")
      
      val stockSymbol = "AAPL"
      val apiKey = "481J3F4MT6YQUEI3" // Remplacez par votre clé API Alpha Vantage
      val url = uri"https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=$stockSymbol&apikey=$apiKey"
      
      println(s"Envoi de la requête API vers : $url")
      
      val request = basicRequest.get(url).send(backend)
      
      request.body match {
        case Right(jsonString) =>
          println(s"Réponse brute de l'API : $jsonString")
          
          extractPriceFromJson(jsonString) match {
            case Some(price) =>
              println(s"Prix extrait avec succès : $stockSymbol = $price dollars")
              portfolioActor ! MarketDataUpdate(stockSymbol, price)
            case None =>
              println("Échec de l'extraction du prix à partir du JSON.")
          }

        case Left(error) =>
          println(s"Erreur lors de l'appel API : $error")
      }
  }

  def extractPriceFromJson(json: String): Option[Double] = {
    Try {
      val jsonAst = json.parseJson
      val globalQuote = jsonAst.asJsObject.fields("Global Quote").asJsObject.fields
      globalQuote.get("05. price") match {
        case Some(JsString(priceStr)) =>
          println(s"Extraction réussie : prix trouvé = $priceStr")
          Some(priceStr.toDouble)
        case _ =>
          println("Aucun prix trouvé dans la réponse JSON.")
          None
      }
    }.toOption.flatten
  }
}

object MarketDataActor {
  def props(portfolioActor: ActorRef): Props = Props(new MarketDataActor(portfolioActor))
}