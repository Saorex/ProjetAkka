ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "Projet-Akka",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      // Akka Actor Classic (Gestion des acteurs)
      "com.typesafe.akka" %% "akka-actor" % "2.8.0",
      "com.typesafe.akka" %% "akka-stream" % "2.6.20",
      "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20",

      // STTP Client pour faire des requêtes HTTP
      "com.softwaremill.sttp.client3" %% "core" % "3.9.0",
      "com.softwaremill.sttp.client3" %% "akka-http-backend" % "3.8.15",

      // JSON Parsing avec Circe
      "io.circe" %% "circe-core" % "0.14.5",
      "io.circe" %% "circe-generic" % "0.14.5",
      "io.circe" %% "circe-parser" % "0.14.5",

      // Logging avec Logback
      "ch.qos.logback" % "logback-classic" % "1.4.7",

      // Testing avec ScalaTest et Akka TestKit
      "org.scalatest" %% "scalatest" % "3.2.16" % Test,
      "com.typesafe.akka" %% "akka-testkit" % "2.8.0" % Test
    )
  )

// Activation du plugin sbt-assembly pour générer un fat-jar
enablePlugins(AssemblyPlugin)

// Stratégie de fusion pour éviter les conflits META-INF dans le fat-jar
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

// Configuration pour que le fat-jar démarre avec une classe principale
Compile / mainClass := Some("Main")