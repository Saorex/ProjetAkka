error id: file:///H:/Desktop/ING2/S2/ProjetAkka/src/main/scala/projetAkka/Main.scala:9
file:///H:/Desktop/ING2/S2/ProjetAkka/src/main/scala/projetAkka/Main.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
|empty definition using fallback
non-local guesses:
	 -

Document text:

```scala
import akka.actor.ActorSystem

object FinanceApp extends App {
  val system = ActorSystem("FinanceSystem")

  // Création des acteurs
  val portfolioActor = system.actorOf(PortfolioActor.props(), "portfolio")
  val marketDataActor = system.actorOf(MarketDataActor.props(portfolioActor), "marketData")

  // Afficher le portefeuille après 10 secondes
  import system.dispatcher
  system.scheduler.scheduleOnce(10.seconds) {
    portfolioActor ! ShowPortfolio
  }
}

```

#### Short summary: 

empty definition using pc, found symbol in pc: 