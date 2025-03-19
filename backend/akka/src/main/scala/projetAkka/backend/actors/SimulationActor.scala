
package projetAkka.backend.actors

import akka.actor.Actor

// Messages pour l'acteur Simulation
case class SimulateInvestment(initialAmount: Double, timeHorizon: Int, annualReturnRate: Double, annualFees: Double)
case class SimulationResult(data: Seq[Map[String, Double]])

class SimulationActor extends Actor {
  def receive = {
    case SimulateInvestment(initialAmount, timeHorizon, annualReturnRate, annualFees) =>
      val results = simulateInvestment(initialAmount, timeHorizon, annualReturnRate, annualFees)
      sender() ! SimulationResult(results)
  }

  def simulateInvestment(initialAmount: Double, timeHorizon: Int, annualReturnRate: Double, annualFees: Double): Seq[Map[String, Double]] = {
    var amountWithFees = initialAmount
    var amountWithoutFees = initialAmount
    (0 until timeHorizon).map { year =>
      amountWithFees = amountWithFees * (1 + (annualReturnRate - annualFees) / 100)
      amountWithoutFees = amountWithoutFees * (1 + annualReturnRate / 100)
      Map("year" -> (year + 1).toDouble, "amountWithFees" -> amountWithFees, "amountWithoutFees" -> amountWithoutFees)
    }
  }
}
