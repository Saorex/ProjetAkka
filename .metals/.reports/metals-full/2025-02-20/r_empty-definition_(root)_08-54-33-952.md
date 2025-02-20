error id: file:///H:/Desktop/ING2/S2/ProjetAkka/backend/src/main/scala/projetAkka/DataVizualizer.scala:28
file:///H:/Desktop/ING2/S2/ProjetAkka/backend/src/main/scala/projetAkka/DataVizualizer.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
|empty definition using fallback
non-local guesses:
	 -

Document text:

```scala
package projetAkka

import org.knowm.xchart.{XYChart, XYChartBuilder, SwingWrapper}
import java.util.Date
import java.time.ZoneId
import scala.jdk.CollectionConverters._

object DataVisualizer {
  def plotData(data: List[AlphaVantageClient.DailyData]): Unit = {
    // Conversion des LocalDate en java.util.Date
    val dates: List[Date] = data.map { d =>
      Date.from(d.date.atStartOfDay(ZoneId.systemDefault()).toInstant)
    }
    val closePrices: List[Double] = data.map(_.close)

    // Création du graphique
    val chart: XYChart = new XYChartBuilder()
      .width(800)
      .height(600)
      .title("Cours de clôture de BNP Paribas")
      .xAxisTitle("Date")
      .yAxisTitle("Prix de clôture")
      .build()

    // Pour éviter les problèmes de typage avec addSeries, on force le type de la liste des dates en List[Any]
    val xData: java.util.List[Any] = dates.map(_.asInstanceOf[Any]).asJava

    // Ajout de la série de données
    chart.addSeries("Close", xData.asInstanceOf[java.util.List[java.util.Date]], closePrices.asJava)

    // Optionnel : personnaliser l'affichage de l'axe X pour les dates
    chart.getStyler.setDatePattern("yyyy-MM-dd")

    // Affichage du graphique dans une fenêtre
    new SwingWrapper(chart).displayChart()
  }
}

```

#### Short summary: 

empty definition using pc, found symbol in pc: 