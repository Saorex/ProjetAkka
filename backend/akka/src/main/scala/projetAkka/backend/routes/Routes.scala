package projetAkka.backend.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

object Routes {
  val routes: Route =
    path("hello") {
      get {
        complete("Sucess")
      }
    }
}
