error id: sttp/
file:///H:/Desktop/ING2/S2/ProjetAkka/src/main/scala/projetAkka/Main.scala
empty definition using pc, found symbol in pc: sttp/
semanticdb not found
|empty definition using fallback
non-local guesses:
	 -

Document text:

```scala
object FinanceApp extends App {
  println("🚀 Démarrage de l'application FinanceApp...")

  val system = ActorSystem("FinanceSystem")
  val portfolioActor = system.actorOf(PortfolioActor.props(), "portfolio")
  val marketDataActor = system.actorOf(MarketDataActor.props(portfolioActor), "marketData")

  println("🎭 Acteurs créés : PortfolioActor et MarketDataActor")

  // Afficher le portefeuille après 10 secondes
  import system.dispatcher
  system.scheduler.scheduleOnce(10.seconds) {
    println("📊 Affichage du portefeuille après 10 secondes...")
    portfolioActor ! ShowPortfolio
  }
}

```

#### Short summary: 

empty definition using pc, found symbol in pc: sttp/