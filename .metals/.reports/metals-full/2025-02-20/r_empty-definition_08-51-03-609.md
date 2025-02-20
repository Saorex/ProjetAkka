error id: 
file:///H:/Desktop/ING2/S2/ProjetAkka/backend/build.sbt
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
|empty definition using fallback
non-local guesses:
	 -

Document text:

```scala
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "Projet-Akka",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      //  Akka Actor Classic (Gestion des acteurs)
      "com.typesafe.akka" %% "akka-actor" % "2.8.0",
      "com.typesafe.akka" %% "akka-stream" % "2.6.20",
      "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20",
      //  STTP Client pour faire des requêtes HTTP vers Yahoo Finance
      "com.softwaremill.sttp.client3" %% "core" % "3.9.0",
      "com.softwaremill.sttp.client3" %% "akka-http-backend" % "3.8.15",

      //  JSON Parsing (Spray JSON)
      "io.spray" %% "spray-json" % "1.3.6",
      

      //  Logging avec Logback
      "ch.qos.logback" % "logback-classic" % "1.4.7",

      //  Testing avec ScalaTest et Akka TestKit
      "org.scalatest" %% "scalatest" % "3.2.16" % Test,
      "com.typesafe.akka" %% "akka-testkit" % "2.8.0" % Test,

      "org.knowm.xchart" % "xchart" % "3.8.1"
      
    )
  )

```

#### Short summary: 

empty definition using pc, found symbol in pc: 