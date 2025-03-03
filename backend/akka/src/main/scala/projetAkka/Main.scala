package projetAkka

import projetAkka.backend.actors._
import projetAkka.backend.routes._

import io.github.cdimascio.dotenv.Dotenv

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.StdIn

import slick.jdbc.PostgresProfile.api._

object Main extends App {

  val dotenv: Dotenv = Dotenv.load()

  // Charger et définir dans les propriétés système
  dotenv.entries().forEach { entry =>
    System.setProperty(entry.getKey, entry.getValue)
  }

  // Vérification
  println(s"Database URL: ${System.getProperty("POSTGRES_URL")}")

  // Initialisation de l'Actor System
  implicit val system: ActorSystem = ActorSystem("InvestmentSystem")

  // Démarrage du serveur HTTP
  implicit val executionContext = system.dispatcher
  val server = Http().newServerAt("localhost", 9090).bind(Routes.routes)

  server.map { _ =>
    println("Successfully started on localhost:9090")
  } recover {
    case ex =>
      println("Failed to start the server due to: " + ex.getMessage)
      system.terminate()
  }

  val db = Database.forConfig("akka.persistence.jdbc.slick.db")
  val testQuery = sql"SELECT * from users".as[Int]

  db.run(testQuery).map(result => println(s"Database is reachable: $result"))
    .recover {
      case ex =>
        println(s"Database connection failed: ${ex.getMessage}")
        ex.printStackTrace()
    }

  // Création des acteurs
  val userActor = system.actorOf(Props[UserActor], "userActor")
  val marketActor = system.actorOf(Props[MarketDataActor], "marketActor")

  // Envoi de messages aux acteurs
  userActor ! CreatePortfolio("User1")
  userActor ! AddStockToPortfolio("AAPL", 10)
  userActor ! AddStockToPortfolio("GOOGL", 5)
  userActor ! ShowPortfolio

  marketActor ! FetchMarketData

  // Attente pour maintenir le serveur en vie
  println("Press ENTER to stop the server...")
  StdIn.readLine()
  system.terminate()
}
