# **Plateforme de Gestion de Portefeuilles Financiers en Temps Réel**  

## **Description**  
Ce projet est une application de gestion de portefeuilles financiers permettant aux utilisateurs de :  
- Suivre en temps réel la valeur de leurs actifs en crypto-monnaies.  
- Recevoir des notifications sur les événements clés du marché.  
- Simuler et analyser des stratégies d’investissement.  

L’application repose sur **Akka** pour la gestion des acteurs et la concurrence, **PostgreSQL** pour le stockage des données, et l’API **Binance** pour l’intégration des flux de marché crypto.  

## **Architecture Technique**  

### **Backend**  
- **Langage** : Scala  
- **Framework** : Akka (Actors & Streams)  
- **Base de données** : PostgreSQL  
- **ORM** : Slick avec JDBC  
- **Sources de données** : API Binance (marché crypto)  
- **Déploiement** : Docker  

### **Frontend (optionnel si inclus dans le projet)**  
- **Technologie** : React.js
- **Mises à jour en temps réel** : WebSocket  
- **Visualisation** : Chart.js 

## **Fonctionnalités**  
✅ **Suivi en temps réel** : Rafraîchissement des prix via l’API Binance toutes les 1 minutes.\
✅ **Analyse de portefeuille** : Graphiques et indicateurs.\
✅ **Gestion d’actifs** : Ajout / suppression de crypto-monnaies au portefeuille.\
✅ **Simulations et prévisions** : Tests de stratégies et prévisions.\
✅ **Sécurisation des données** : Utilisation de fichier d'envrionnement (.env) pour l'accès à la base de données.\

## **Installation**  

**Cloner le repo**  
   ```bash
   git clone [https://github.com/SaoRex/projetAkka.git](https://github.com/Saorex/ProjetAkka.git)
   cd ProjetAkka
   ```

## **Lancement du Projet**
Les étapes de lancement sont détaillé dans d'autre README que ce soit pour un déploiement local ou via docker pour Akka.

### **Local**
Veuillez vous rendre ici :
  ```bash
   cd ProjetAkka/backend/akka
   ```
### **Docker**
Veuillez vous rendre ici :
  ```bash
   cd ProjetAkka/backend/docker/
   ```