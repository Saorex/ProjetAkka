error id: `<error>`#`<error>`.
file:///H:/Desktop/ING2/S2/ProjetAkka/build.sbt
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
|empty definition using fallback
non-local guesses:
	 -libraryDependencies.
	 -libraryDependencies#
	 -libraryDependencies().
	 -scala/Predef.libraryDependencies.
	 -scala/Predef.libraryDependencies#
	 -scala/Predef.libraryDependencies().

Document text:

```scala
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "Projet-Akka",
    version := "1.8.0",
    libraryDependencies ++= Seq(
    // Akka Actor System
    "com.typesafe.akka" %% "akka-actor" % "2.8.0",
    "com.typesafe.akka" %% "akka-stream" % "2.8.0",
    "com.typesafe.akka" %% "akka-http" % "10.5.0",

    // JSON Parsing
    "com.typesafe.play" %% "play-json" % "2.10.0",
    
    // STTP pour les requêtes HTTP
    "com.softwaremill.sttp.client" %% "core" % "2.2.9",

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

```

#### Short summary: 

empty definition using pc, found symbol in pc: 