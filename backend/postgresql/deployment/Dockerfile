FROM openjdk:17

WORKDIR /app

COPY target/scala-2.13/Projet-Akka-assembly-0.1.0.jar projet-akka.jar


COPY src/main/resources/application.conf /app/application.conf


CMD ["java", "-Dconfig.file=/app/application.conf", "-jar", "projet-akka.jar"]
