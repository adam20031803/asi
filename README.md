# AutoLoc API

API REST de gestion de location de véhicules multi-agences (UP ASI — ateliers Spring Boot).

## Stack

Java 17 · Spring Boot 3.3 · Spring Data JPA · MySQL · Lombok · SpringDoc OpenAPI

## Lancer le projet

1. Démarrer MySQL (la base `autoloc_db` est créée automatiquement).
2. Lancer l'application :

   ```bash
   ./mvnw spring-boot:run
   ```

3. L'API est disponible sur `http://localhost:8080/api`.

## Documentation et tests

- Swagger UI : `http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON : `http://localhost:8080/api/v3/api-docs`
- Collection Postman : `postman/AutoLoc.postman_collection.json` + environnement `postman/local.postman_environment.json`

## Ressources exposées

`/agences`, `/employes`, `/vehicules`, `/equipements`, `/clients`, `/reservations`, `/contrats`, `/paiements` (lecture seule), `/maintenances`

## Ateliers

| Atelier | Contenu |
| :-- | :-- |
| 1–2 | Entités JPA et associations |
| 3 | Couche Repository (Spring Data JPA) — `docs/repository-notes.md` |
| 4 | Couche Service |
| 5 | API REST, tests Postman, documentation Swagger — `docs/api-notes.md` |
