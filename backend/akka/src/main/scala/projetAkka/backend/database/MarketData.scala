package projetAkka.backend.database

case class MarketData(symbol: String, timestamp: Long, open: Double, close: Double)