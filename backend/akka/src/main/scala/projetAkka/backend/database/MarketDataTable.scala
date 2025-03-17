package projetAkka.backend.database

import slick.jdbc.PostgresProfile.api._

class MarketDataTable(tag: Tag) extends Table[MarketData](tag, "market_data") {
  def symbol = column[String]("symbol")
  def timestamp = column[Long]("timestamp")
  def open = column[Double]("open")
  def close = column[Double]("close")

  def * = (symbol, timestamp, open, close) <> (MarketData.tupled, MarketData.unapply)
}