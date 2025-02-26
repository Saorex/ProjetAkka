ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "Projet-Akka",
    version := "0.1.0",
    libraryDependencies ++= Seq(
    // Akka Actor System
    "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
    "com.typesafe.akka" %% "akka-stream" % "2.8.0",
    "com.typesafe.akka" %% "akka-http" % "10.5.0",

    // JSON Parsing
    "com.typesafe.play" %% "play-json" % "2.10.0",

    // Database (si vous utilisez PostgreSQL)
    "org.postgresql" % "postgresql" % "42.6.0",
    "com.typesafe.slick" %% "slick" % "3.4.1",

    // Logging
    "ch.qos.logback" % "logback-classic" % "1.4.7",

    // Testing
    "org.scalatest" %% "scalatest" % "3.2.16" % Test,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.8.0" % Test
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
Compile / mainClass := Some("projetAkka.Main")
 