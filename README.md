# 🚀 Projet PING

## 📋 Présentation
Bienvenue dans le projet PING ! Ce projet vise à développer nos compétences en développement logiciel tout au long de l'année en construisant une application complète.

## 🎯 Objectifs
- Mener un projet complet de A à Z.
- Monter en compétence sur un écosystème Java moderne (Quarkus).
- Appliquer les bonnes pratiques de développement et de travail en équipe.

## 👥 Équipe
- Martin Lemetais
- Dardan Bytyqi
- Hugo Viala
- Nathan Fontaine
- Khoren Pasdrmadjian

## 🛠️ Stack Technique
Ce projet est construit avec les technologies suivantes :
- **Langage** : Java 21
- **Framework** : Quarkus
- **Gestion de projet** : Maven
- **Base de données** : PostgreSQL
- **ORM** : Hibernate ORM avec Panache
- **Tests** : JUnit 5 & REST Assured

## ⚡ Démarrage Rapide

### Prérequis
- JDK 21+
- Maven 3.8+
- Docker (pour la base de données si non installée localement)

### Lancer le projet en mode développement
On clean le projet et on install le projet
```bash
mvn clean install
```
Pour lancer l'application en mode développement, qui offre le rechargement à chaud (hot-reload) :
```bash
mvn quarkus:dev
```
L'application sera alors accessible sur `http://localhost:8080`.

### Lancer les tests
Pour exécuter la suite de tests unitaires et d'intégration :
```bash
mvn test
```

## 📖 Documentation de l'API
Une fois l'application lancée, la documentation complète de l'API est disponible via Swagger UI à l'adresse suivante :
[http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)

## 🌿 Workflow Git & Déploiement

### Stratégie de Branches
- La branche `master` est la branche principale de développement. Tout le travail quotidien doit être intégré sur celle-ci.

### Dépôts Distants (Remotes)
Nous utilisons deux dépôts distants avec des rôles distincts :
- `development` : Le dépôt principal pour le travail de tous les jours. **C'est ici qu'il faut `push` quotidiennement.**
  ```bash
  # Pour envoyer les dernières modifications
  git push development master
  ```
- `origin` : Le dépôt **uniquement pour la soumission finale à l'école**. Ne pas `push` dessus sauf pour les rendus officiels.
  ```bash
  # Pour soumettre le projet (voir la procédure spéciale ci-dessous)
  git push origin [branche_de_soumission]:master --force
  ```

## ⚠️ Règles de Soumission
Pour que le projet soit validé par la moulinette de l'école, il est **impératif** de respecter les points suivants.

### 1. Arborescence des fichiers
La structure du dépôt de soumission doit être **exactement** comme suit. Seuls les fichiers listés (et leurs enfants) doivent être présents. Le `.gitignore` est configuré pour aider, mais la vigilance est de mise.

```
ping/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── fr/epita/assistants/ping/
    │   │       └── ... (tous vos packages et fichiers .java que nous avons créés)
    │   └── resources/
    │       ├── application.properties
    │       └── openapi.yml
    └── test/
        └── java/
            └── fr/epita/assistants/ping/
                └── ... (tous vos fichiers de test .java que nous avons créés)
```

### 2. Contrainte sur le `pom.xml`
Le `pom.xml` que vous utilisez pour le développement local **ne sera pas celui utilisé pour la correction**. Il sera remplacé par un `pom.xml` officiel fourni par l'école.

- **Conséquence directe** : Si vous ajoutez une dépendance dans votre `pom.xml` local pour une fonctionnalité, le code ne compilera pas pour la correction. Le projet doit pouvoir fonctionner avec le `pom.xml` de base.

- **Procédure de vérification OBLIGATOIRE avant de `push` sur `development`** :
  1. Procurez-vous la version officielle du `pom.xml` de l'école.
  2. Remplacez votre `pom.xml` par celui de l'école.
  3. Lancez la commande `mvn clean verify` depuis la racine du dossier `ping`.
  4. Si la commande échoue, votre code n'est pas compatible. Vous devez corriger les erreurs (souvent des `import` de dépendances non autorisées) avant de pouvoir envoyer votre travail.
  5. Une fois la vérification passée, vous pouvez restaurer votre `pom.xml` de développement pour continuer à travailler.

### 3. Nom des modèles
Les noms des classes de modèle (ex: `UserModel`, `ProjectModel`) et les autres classes fournies dans le squelette initial ne doivent pas être changés.