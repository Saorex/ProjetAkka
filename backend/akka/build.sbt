import sbt._
import sbt.Keys._
import sbtassembly.AssemblyPlugin

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"

// Repository pour Akka Persistence JDBC
ThisBuild / resolvers += "Lightbend Repository".at("https://repo.lightbend.com/lightbend/maven-releases/")

// Configuration des tâches de compilation
Compile / run := (Compile / run).dependsOn(Compile / compile).evaluated

// Définition du projet
lazy val root = (project in file("."))
  .settings(
    name := "Projet-Akka",
    version := "0.1.0",
    Compile / unmanagedResourceDirectories += baseDirectory.value / "src" / "main" / "resources",
    libraryDependencies ++= Seq(
      // Akka Actor System
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
      "com.typesafe.akka" %% "akka-stream" % "2.8.0",
      "com.typesafe.akka" %% "akka-http" % "10.2.9",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.9",

      // Akka Persistence
      "com.typesafe.akka" %% "akka-persistence-typed" % "2.8.0",
      "de.heikoseeberger" %% "akka-http-play-json" % "1.39.2", // Ajout du support Play JSON
      "com.typesafe.akka" %% "akka-persistence-query" % "2.8.0",
      "com.lightbend.akka" %% "akka-persistence-jdbc" % "5.2.0",

      // JDBC Driver PostgreSQL
      "org.postgresql" % "postgresql" % "42.6.0",
      "org.mindrot" % "jbcrypt" % "0.4",
      "ch.megard" %% "akka-http-cors" % "1.1.2",

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

// Configuration Docker
enablePlugins(JavaAppPackaging, DockerPlugin)

dockerBaseImage := "openjdk:17"
dockerExposedPorts ++= Seq(8080, 9000)
dockerUsername := Some("saorex")
dockerRepository := Some("saorex")
dockerAlias := dockerAlias.value.withRegistryHost(Some("docker.io"))
