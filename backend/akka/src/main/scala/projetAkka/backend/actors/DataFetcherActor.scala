package projetAkka.backend.actors

import projetAkka.backend.database._

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.scaladsl.{Flow, RunnableGraph, Sink, Source}
import spray.json._
import DefaultJsonProtocol._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class DataFetcherActor(apiUrl: String,symbol: String) extends Actor with ActorLogging {
  implicit val system = context.system
  implicit val ec: ExecutionContext = system.dispatcher

  override def preStart(): Unit = {
    log.info("DataFetcherActor started, scheduling API requests.")

    val source = Source.tick(0.seconds, 1.minute, "fetch")

    val httpFlow = Flow[String]
      .mapAsync(1)(_ => {
        log.info(s"Requesting Binance API at $apiUrl")
        Http().singleRequest(HttpRequest(uri = apiUrl))
      })
      .mapAsync(1) { response =>
        if (response.status.isSuccess()) {
          response.entity.dataBytes
            .runFold("")(_ + _.utf8String)
            .map(Some(_))
        } else {
          log.error(s"HTTP request failed with status: ${response.status}")
          response.discardEntityBytes()
          scala.concurrent.Future.successful(None)
        }
      }
      .collect { case Some(jsonString) => jsonString }
      .map { jsonString =>
        log.info(s"Réponse complète de Binance API : $jsonString")

        val parsedJson = jsonString.parseJson
        val klineArray = parsedJson.convertTo[JsArray].elements.headOption.getOrElse {
          throw new RuntimeException("No data received from Binance API")
        }.convertTo[JsArray].elements

        val openTime = klineArray(0).convertTo[Long]
        val open = klineArray(1).convertTo[String].toDouble
        val close = klineArray(4).convertTo[String].toDouble

        log.info(s"MarketData - symbol: $symbol, timestamp: $openTime, open: $open, close: $close")

        MarketData(symbol, openTime, open, close)
      }

    val dbSink = Sink.foreach[MarketData](MarketDataRepository.insertMarketData)

    val graph: RunnableGraph[_] = source.via(httpFlow).to(dbSink)
    graph.run()
  }

  override def receive: Receive = Actor.emptyBehavior
}
