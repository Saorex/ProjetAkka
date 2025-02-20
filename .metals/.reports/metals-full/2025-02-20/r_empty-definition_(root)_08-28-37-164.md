error id: file:///H:/Desktop/ING2/S2/ProjetAkka/backend/src/main/scala/projetAkka/Main.scala:16
file:///H:/Desktop/ING2/S2/ProjetAkka/backend/src/main/scala/projetAkka/Main.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
|empty definition using fallback
non-local guesses:
	 -

Document text:

```scala
import sttp.client3._
import java.time.format.DateTimeFormatter
import java.time.LocalDate

object AlphaVantageClient {
  private val API_KEY = "F6C5LYEDEY3TPUT2"
  private val SYMBOL = "BNP.PA" // bnp paribas 
  private val BASE_URL = "https://www.alphavantage.co/query"

  def main(args: Array[String]): Unit = {
  val data = fetchCAC40DailyData()
  println("Données parsées :")
  data.foreach { dailyData =>
    println(s"Date: ${dailyData.date}, Open: ${dailyData.open}, Close: ${dailyData.close}, Volume: ${dailyData.volume}")
  }

  // Lancer la visualisation
    val visualizer = new DataVisualizer(data)
    visualizer.main(Array())
  }
}

  def fetchCAC40DailyData(): List[DailyData] = {
  val request = basicRequest
    .get(uri"$BASE_URL?function=TIME_SERIES_DAILY&symbol=$SYMBOL&apikey=$API_KEY")
    .response(asString)

  val backend = HttpURLConnectionBackend()
  val response = request.send(backend)

  // Vérifier le statut HTTP
  println(s"Statut HTTP de la réponse : ${response.code}")

  response.body match {
    case Right(json) =>
      // Afficher la réponse JSON brute
      println("Réponse brute de l'API :")
      println(json)
      // Parser la réponse
      parseAlphaVantageResponse(json)
    case Left(error) =>
      println(s"Erreur lors de la récupération des données: $error")
      List.empty
  }
}

  // Suppression du mot-clé `private`
  case class DailyData(
    date: LocalDate,
    open: Double,
    high: Double,
    low: Double,
    close: Double,
    volume: Long
  )

  private def parseAlphaVantageResponse(json: String): List[DailyData] = {
    val pattern = """"(\d{4}-\d{2}-\d{2})": \{.+?"1\. open": "(\d+\.\d+)",.+?"2\. high": "(\d+\.\d+)",.+?"3\. low": "(\d+\.\d+)",.+?"4\. close": "(\d+\.\d+)",.+?"5\. volume": "(\d+)".+?\}""".r

    pattern.findAllIn(json).map { matchStr =>
      val groups = pattern.unapplySeq(matchStr).get
      DailyData(
        LocalDate.parse(groups(0), DateTimeFormatter.ISO_DATE),
        groups(1).toDouble,
        groups(2).toDouble,
        groups(3).toDouble,
        groups(4).toDouble,
        groups(5).toLong
      )
    }.toList.sortBy(_.date)(Ordering[LocalDate].reverse)
  }
}
```

#### Short summary: 

empty definition using pc, found symbol in pc: 