error id: _empty_/FinanceApp.marketDataActor.
file:///H:/Desktop/ING2/S2/ProjetAkka/src/main/scala/projetAkka/Main.scala
empty definition using pc, found symbol in pc: 
found definition using semanticdb; symbol _empty_/FinanceApp.marketDataActor.
|empty definition using fallback
non-local guesses:
	 -

Document text:

```scala
import akka.actor.ActorSystem
import scala.concurrent.duration._

object FinanceApp extends App {
  val system = ActorSystem("FinanceSystem")
  println("🚀 Démarrage de l'application FinanceApp...")

  // 📌 Création des acteurs
  val portfolioActor = system.actorOf(PortfolioActor.props(), "portfolio")
  val marketDataActor = system.actorOf(MarketDataActor.props(portfolioActor), "marketData")

  // 📌 Afficher le portefeuille après 10 secondes
  import system.dispatcher
  system.scheduler.scheduleOnce(10.seconds) {
    portfolioActor ! ShowPortfolio
  }
}

```

#### Short summary: 

empty definition using pc, found symbol in pc: 