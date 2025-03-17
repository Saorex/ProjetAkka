package projetAkka.backend

import projetAkka.backend.actors._
import projetAkka.backend.routes._
import projetAkka.backend.database._

import io.github.cdimascio.dotenv.Dotenv
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Main extends App {

  // Chargement des variables d'environnement depuis .env
  val dotenv: Dotenv = Dotenv.configure()
    .directory("C:\\Users\\acer\\Documents\\ProjetAkka-main\\backend\\docker\\deployment")
    .load()

  dotenv.entries().forEach { entry =>
    System.setProperty(entry.getKey, entry.getValue)
  }

  // Initialisation de l'Actor System
  implicit val system: ActorSystem = ActorSystem("InvestmentSystem")

  // Démarrage du serveur HTTP
  implicit val executionContext = system.dispatcher
  val server = Http().newServerAt("localhost", 9090).bind(Routes.routes)

  implicit val system: ActorSystem = ActorSystem("InvestmentSystem")
  implicit val executionContext: ExecutionContext = system.dispatcher

  // Connexion à la base de données PostgreSQL avec gestion d'erreur
  val db = try {
    Database.forURL(
      url = dbUrl,
      user = dbUser,
      password = dbPassword,
      driver = "org.postgresql.Driver"
    )
  } catch {
    case ex: Exception =>
      println(s" Erreur de connexion à PostgreSQL: ${ex.getMessage}")
      system.terminate()
      throw ex
  }

  //Récuppération donnée
  val symbols = List(
    "BTCUSDT",
    "ETHUSDT",
    "BNBUSDT",
    "SOLUSDT",
    "AVAXUSDT",
    "INJUSDT",
    "NEARUSDT",
    "TIAUSDT",
    "PYTHUSDT",
    "WIFUSDT",
    "JUPUSDT",
  )
  val dataFetcher = system.actorOf(Props(new DataFetcherActor(symbols)), "dataFetcher")

  // Création des acteurs
  val userActor = system.actorOf(Props[UserActor], "userActor")
  val marketActor = system.actorOf(Props[MarketDataActor], "marketActor")
  val simulationActor = system.actorOf(Props[SimulationActor], "simulationActor")
  val marketActor = system.actorOf(Props[MarketDataActor], "marketDataActor")
  val marketManager = system.actorOf(Props(new MarketManagerActor(marketActor)), "marketManager")

  // Authentification avec UserRepository
  val userRepository = new UserRepository(db)
  val authActor = system.actorOf(Props(new AuthActor(userRepository)), "authActor")

  // Définition des routes Akka HTTP
  val authRoutes = new AuthRoutes(authActor).route
  val allRoutes = concat(Routes.routes, authRoutes)

  // Lancement du serveur HTTP Akka avec gestion d'erreur
  val server = try {
    Http().newServerAt("localhost", 8080).bind(allRoutes)
  } catch {
    case ex: Exception =>
      println(s" Erreur lors du démarrage du serveur: ${ex.getMessage}")
      system.terminate()
      throw ex
  }

  server.map { _ =>
    println(" Serveur démarré sur http://localhost:8080")
  } recover {
    case ex =>
      println(s" Erreur au démarrage du serveur: ${ex.getMessage}")
      system.terminate()
  }

  // Exemple de simulation
  simulationActor ! SimulateInvestment(10000, 10, 5, 1)
  
  // Attente pour maintenir le serveur en vie
  println("Press ENTER to stop the server...")
  StdIn.readLine()
  system.terminate()
}
