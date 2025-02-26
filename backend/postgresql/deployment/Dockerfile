FROM openjdk:17-jdk-slim

WORKDIR /app

#Copie du fichier fat JAR généré par sbt assembly et .env
COPY ../../akka/target/scala-2.13/Projet-Akka-assembly-0.1.0.jar projet-akka.jar
COPY ../../postgresql/deployment/.env .env


CMD ["java", "-jar", "projet-akka.jar"]

