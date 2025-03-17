package projetAkka.backend

import projetAkka.backend.actors.{AuthActor, MarketDataActor, MarketManagerActor, UserActor}
import projetAkka.backend.routes.{AuthRoutes, Routes}
import projetAkka.backend.database.UserRepository
import io.github.cdimascio.dotenv.Dotenv
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
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

  // Vérification des variables de connexion PostgreSQL
  val dbUrl = System.getProperty("POSTGRES_URL", "jdbc:postgresql://localhost:5432/akka")
  val dbUser = System.getProperty("POSTGRES_USER", "admin")
  val dbPassword = System.getProperty("POSTGRES_PASSWORD", "B9&!hm%dQ@GLWSrEWC8j")

  println(s" Vérification de la configuration PostgreSQL :")
  println(s"   - URL: $dbUrl")
  println(s"   - USER: $dbUser")
  println(s"   - PASSWORD: ******** ")

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

  // Création des acteurs
  val marketActor = system.actorOf(Props[MarketDataActor], "marketDataActor")
  val marketManager = system.actorOf(Props(new MarketManagerActor(marketActor)), "marketManager")
  val user = system.actorOf(Props[UserActor], "userActor")

  // Authentification avec UserRepository
  val userRepository = new UserRepository(db)
  val authActor = system.actorOf(Props(new AuthActor(userRepository)), "authActor")

  // Définition des routes Akka HTTP
  val authRoutes = new AuthRoutes(authActor).route
  val allRoutes: Route = Routes.routes(authActor) ~ authRoutes  

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

  println("Appuie sur ENTRÉE pour arrêter le serveur.")
  StdIn.readLine()
  system.terminate()
}
