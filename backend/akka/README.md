# Documentation - Lancer le projet Akka localement avec SBT

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
cd ./backend/docker/deployment

Lancer Docker Desktop

Puis dans le back :
docker compose up -d postgresDB --build
```

### 2. Installer les dépendances et compiler le projet

```bash
cd ../../akka
sbt clean compile
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
cd ./backend/docker/deployement
docker-compose down
```

# API Endpoints Documentation

## Introduction
Cette documentation décrit les différents endpoints de l'API et leur utilisation.

## Endpoints

### 1. Authentification
#### `POST /api/login`
**Description :** Permet aux utilisateurs de se connecter et d'obtenir un token d'authentification.

**Corps de la requête (JSON) :**
```json
{
  "username": "exemple",
  "password": "motdepasse"
}
```

**Réponse si les champs sont corrects (JSON) :**
```json
{
  "token": "jwt_token"
}
```

---

### 2. Données de marché
#### `GET /api/data?symbol={SYMBOL}`
**Description :** Récupère les données de marché pour un symbole donné.

**Paramètres :**
- `symbol` (query param) : Le symbole de la cryptomonnaie (ex: `BTCUSDT`).

**Exemple de requête :**
```
GET /api/data?symbol=BTCUSDT
```

**Réponse (JSON) :**
```json
[
  {
    "symbol": "BTCUSDT",
    "timestamp": 1710183600,
    "open": 62000.0,
    "close": 62500.0
  }
]
```

---

### 3. Simulations d'investissement
#### `POST /api/simulations`
**Description :** Permet de simuler un investissement sur un actif.

## Notes
- Tous les endpoints sont accessibles via `http://localhost:8080`.
- L'authentification est requise pour certaines routes (ajouter un header `Authorization: Bearer {token}`).

