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

 