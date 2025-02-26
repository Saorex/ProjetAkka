# Documentation - Lancer un projet Akka avec SBT

## Prérequis

Avant de démarrer le projet, assurez-vous d'avoir installé les éléments suivants :

- [Java JDK 11 ou supérieur](https://adoptopenjdk.net/)
- [SBT](https://www.scala-sbt.org/download.html)
- [Docker & Docker Compose](https://docs.docker.com/get-docker/)

### Configuration requise

- Placez le fichier `.env` fourni par l'administrateur à la racine du projet, sous `./akka/.env`.

## Installation et lancement

### 1. Lancer la base de données avec Docker Compose

```bash
cd ./backend/postgresql/deployement
docker-compose up -d --build
```

### 2. Installer les dépendances et compiler le projet

```bash
cd ../../akka
sbt compile
```

### 4. Lancer l'application

```bash
sbt run
```

## Tests

Pour exécuter les tests unitaires du projet, utilisez la commande suivante :

```bash
sbt test
```

## Arrêter et nettoyer les services Docker

```bash
cd ./backend/postgresql/deployement
docker-compose down
```

