package projetAkka.backend.database

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future

object MarketDataRepository {

  val db = Database.forConfig("akka.persistence.jdbc.slick.db")
  val marketDataTable = TableQuery[MarketDataTable]

  def insertMarketData(marketData: MarketData): Future[Int] = {
    db.run(marketDataTable += marketData)
  }
}
