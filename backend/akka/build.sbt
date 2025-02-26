ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"

// Repository pour Akka Persistence JDBC
ThisBuild / resolvers += "Lightbend Repository".at("https://repo.lightbend.com/lightbend/maven-releases/")

lazy val root = (project in file("."))
  .settings(
      name := "Projet-Akka",
      version := "0.1.0",
      libraryDependencies ++= Seq(
          // Akka Actor System
          "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
          "com.typesafe.akka" %% "akka-stream" % "2.8.0",
          "com.typesafe.akka" %% "akka-http" % "10.5.0",

          // Akka Persistence
          "com.typesafe.akka" %% "akka-persistence-typed" % "2.8.0",
          "com.typesafe.akka" %% "akka-persistence-query" % "2.8.0",
          "com.lightbend.akka" %% "akka-persistence-jdbc" % "5.2.0",

          // JDBC Driver PostgreSQL
          "org.postgresql" % "postgresql" % "42.6.0",

          // Slick pour interagir avec PostgreSQL
          "com.typesafe.slick" %% "slick" % "3.4.1",
          "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",

          // JSON Parsing
          "com.typesafe.play" %% "play-json" % "2.10.0",

          // Logging
          "ch.qos.logback" % "logback-classic" % "1.4.7",

          // Testing
          "org.scalatest" %% "scalatest" % "3.2.16" % Test,
          "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.8.0" % Test,

          // Gestion des variables d'environnement
          "io.github.cdimascio" % "dotenv-java" % "3.0.0"
      )
  )

// Activation du plugin sbt-assembly pour générer un fat-jar
enablePlugins(AssemblyPlugin)

// Stratégie de fusion pour éviter les conflits META-INF dans le fat-jar
assembly / assemblyMergeStrategy := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case _ => MergeStrategy.first
}

// Configuration pour que le fat-jar démarre avec une classe principale
Compile / mainClass := Some("projetAkka.Main")
