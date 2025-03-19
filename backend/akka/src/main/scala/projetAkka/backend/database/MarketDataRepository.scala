package projetAkka.backend.database

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future

case class MarketData(symbol: String, timestamp: Long, open: Double, close: Double)

class MarketDataTable(tag: Tag) extends Table[MarketData](tag, "market_data") {
  def symbol = column[String]("symbol")
  def timestamp = column[Long]("timestamp")
  def open = column[Double]("open")
  def close = column[Double]("close")

  def * = (symbol, timestamp, open, close) <> (MarketData.tupled, MarketData.unapply)
}

object MarketDataRepository {

  val db = Database.forConfig("akka.persistence.jdbc.slick.db")
  val marketDataTable = TableQuery[MarketDataTable]

  def insertMarketData(marketData: MarketData): Future[Int] = {
    db.run(marketDataTable += marketData)
  }

  def getMarketDataBySymbol(symbol: String): Future[Seq[MarketData]] = {
    db.run(marketDataTable.filter(_.symbol === symbol).result)
  }
}
