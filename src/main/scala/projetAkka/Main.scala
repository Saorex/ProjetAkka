package com.tonprojet

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object Main extends App {
  val rootBehavior = Behaviors.setup[String] { context =>
    context.log.info("Akka System is running!")
    Behaviors.empty
  }

  val system = ActorSystem(rootBehavior, "ActeurTest")
}
