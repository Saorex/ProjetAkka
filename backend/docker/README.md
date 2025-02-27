# Documentation de déploiement avec Docker

## Prérequis
Assurez-vous d'avoir **Docker** et **Docker Compose** installés sur votre machine.

Il faut ajouter le fichier .env fourni par l'administrateur dans le dossier /deployment
Pour vérifier qu'il est bien présent :

```sh
cd ./backend/docker/deployment
ls -la .env
```

N'oubliez pas d'ajouter .env dans votre .gitignore.

## Déploiement des conteneurs

### 1. Construire l'image du backend localement
Dans le répertoire racine du projet, exécutez :

1. **Nettoyer et construire l'application**
   ```sh
   cd ./backend/akka
   sbt clean compile
   ```
   
2. **Construire l'image Docker localement**
   ```sh
   sbt docker:publishLocal
   ```

3. **Vérifier que l'image est bien créée**
   ```sh
   docker images | grep saorex/projet-akka
   ```

### 2. Démarrer les services
Lancez les conteneurs en arrière-plan avec :

```sh
   cd ../docker/deployment
   docker compose up -d --build --force-recreate
```

Cela démarre la base de données **PostgreSQL** et le backend **Akka**.

### 3. Vérifier les conteneurs en cours d'exécution
Pour voir les services actifs, utilisez :

```sh
docker ps
```

Vous devriez voir les conteneurs **postgresDB** et **akka** en cours d'exécution.

### 4. Consulter les logs (en cas de problème)
Si votre service ne fonctionne pas comme prévu, consultez les logs :

```sh
docker compose logs <nom-du-service>
```

Cela permet d'afficher les journaux en temps réel afin de détecter d'éventuelles erreurs.

## Arrêter les services
Si vous souhaitez arrêter les conteneurs, utilisez la commande suivante :

```sh
docker compose down
```
Cela stoppe et supprime les conteneurs créés par `docker-compose up`.

## Utilisation du conteneur PostgreSQL

### 1. Accéder au conteneur PostgreSQL

Ouvrez un terminal interactif dans le conteneur PostgreSQL :

```sh
docker exec -it postgresDB psql -U admin -d akka
```

### 2. Lister des tables 
Une fois connecté à PostgreSQL, faites les requêtes SQL voulu :

```sql
SELECT * from users;
```

### 4. Quitter PostgreSQL
Pour sortir de PostgreSQL, utilisez la commande :

```sh
\q
#ou
exit
```

