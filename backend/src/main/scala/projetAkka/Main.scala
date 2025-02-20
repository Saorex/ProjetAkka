import sttp.client3._
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import io.circe._
import io.circe.parser._
import io.circe.generic.auto._
import scala.util.{Try, Success, Failure}

object AlphaVantageClient {
  private val API_KEY = sys.env.getOrElse("7QY18GKR931C5FW8", "DEMO")
  private val SYMBOL = "AAPL"
  private val BASE_URL = "https://www.alphavantage.co/query"

  case class IntradayData(time: String, open: Double, high: Double, low: Double, close: Double, volume: Long)

  def main(args: Array[String]): Unit = {
    fetchIntradayData() match {
      case Some(data) =>
        println("📊 Données récupérées :")
        data.sortBy(_.time).foreach { intradayData =>
          println(s"🕒 ${intradayData.time} | Open: ${intradayData.open} | Close: ${intradayData.close} | Volume: ${intradayData.volume}")
        }
      case None =>
        println("❌ Impossible de récupérer les données.")
    }
  }

  def fetchIntradayData(): Option[List[IntradayData]] = {
    val request = basicRequest
      .get(uri"$BASE_URL?function=TIME_SERIES_INTRADAY&symbol=$SYMBOL&interval=1min&apikey=$API_KEY")
      .response(asString)

    val backend = HttpURLConnectionBackend()
    val response = request.send(backend)

    println(s"📡 Statut HTTP : ${response.code}")

    response.body match {
      case Right(json) =>
        println("📥 Réponse brute de l'API reçue.")
        parseAlphaVantageResponse(json)
      case Left(error) =>
        println(s"⚠️ Erreur HTTP : $error")
        None
    }
  }

  private def parseAlphaVantageResponse(json: String): Option[List[IntradayData]] = {
  parse(json) match {
    case Left(parsingError) =>
      println(s"❌ Erreur de parsing JSON : ${parsingError.message}")
      None
    case Right(jsonObj) =>
      val cursor = jsonObj.hcursor
      val timeSeries = cursor.downField("Time Series (1min)").as[Map[String, Json]]

      timeSeries match {
        case Left(_) =>
          println("⚠️ Format JSON inattendu ou quota dépassé.")
          None
        case Right(data) =>
          val parsedData = data.toList.flatMap { case (time, jsonData) =>
            val cursor = jsonData.hcursor

            // Utilisation de flatMap pour gérer les erreurs
            for {
              open   <- cursor.get[Double]("1. open").toOption
              high   <- cursor.get[Double]("2. high").toOption
              low    <- cursor.get[Double]("3. low").toOption
              close  <- cursor.get[Double]("4. close").toOption
              volume <- cursor.get[Long]("5. volume").toOption
            } yield IntradayData(time, open, high, low, close, volume)
          }
          Some(parsedData)
      }
  }
}

}
