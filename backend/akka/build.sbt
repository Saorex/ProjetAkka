import sbtassembly.AssemblyPlugin
import sbt.Keys._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"

// Repository pour Akka Persistence JDBC
ThisBuild / resolvers += "Lightbend Repository".at("https://repo.lightbend.com/lightbend/maven-releases/")

Compile / run := (Compile / run).dependsOn(Compile / compile).evaluated

lazy val root = (project in file("."))
  .enablePlugins(AssemblyPlugin)
  .settings(
    name := "Projet-Akka",
    version := "0.1.0",

    Compile / unmanagedResourceDirectories += baseDirectory.value / "src" / "main" / "resources",

    libraryDependencies ++= Seq(
      //  Akka Core
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
      "com.typesafe.akka" %% "akka-stream" % "2.8.0",
      "com.typesafe.akka" %% "akka-http" % "10.2.10",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.10",
      "io.spray" %% "spray-json" % "1.3.6",
      "ch.megard" %% "akka-http-cors" % "1.1.2",
      //  Akka Persistence
      "com.typesafe.akka" %% "akka-persistence" % "2.8.0",
      "com.typesafe.akka" %% "akka-persistence-query" % "2.8.0",
      "com.lightbend.akka" %% "akka-persistence-jdbc" % "5.2.0",

      //  PostgreSQL Driver
      "org.postgresql" % "postgresql" % "42.6.0",

      //  Slick pour la connexion DB
      "com.typesafe.slick" %% "slick" % "3.4.1",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",

      //  Sécurité : Hash de mot de passe
      "com.github.t3hnar" %% "scala-bcrypt" % "4.3.0",

      //  JSON Parsing
      "com.typesafe.play" %% "play-json" % "2.10.0",

      // Logging
      "ch.qos.logback" % "logback-classic" % "1.4.7",

      // Testing
      "org.scalatest" %% "scalatest" % "3.2.16" % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.8.0" % Test,

      //  Gestion des variables d'environnement
      "io.github.cdimascio" % "dotenv-java" % "3.0.0"
    ),

    // Gestion des conflits de dépendances
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", _*)                                   => MergeStrategy.discard
      case PathList("META-INF", "versions", "9", "module-info.class") => MergeStrategy.discard
      case PathList("module-info.class")                              => MergeStrategy.discard
      case PathList("org", "slf4j", _*)                               => MergeStrategy.first
      case PathList("com", "fasterxml", "jackson", _*)               => MergeStrategy.first
      case _ => MergeStrategy.first
    }
  )

//  Configuration Docker
enablePlugins(JavaAppPackaging, DockerPlugin)

dockerBaseImage := "openjdk:17"
dockerExposedPorts ++= Seq(8080, 9000)
dockerUsername := Some("saorex")
dockerRepository := Some("saorex")
dockerAlias := dockerAlias.value.withRegistryHost(Some("docker.io"))
