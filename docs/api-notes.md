# Atelier 5 : Spring MVC et SpringDoc — Notes sur l'API REST AutoLoc

- URL de base : `http://localhost:8080/api` (préfixe défini une seule fois par `server.servlet.context-path=/api`).
- Documentation : `http://localhost:8080/api/swagger-ui.html` (Swagger UI) et `http://localhost:8080/api/v3/api-docs` (OpenAPI JSON).
- Collection Postman : `postman/AutoLoc.postman_collection.json` + environnement `postman/local.postman_environment.json` (variable `baseUrl`).
- Erreurs : `GlobalExceptionHandler` renvoie un corps JSON `{ "statut", "message", "horodatage" }`.

---

## A. Endpoints

Les identifiants suivent le modèle : `idContrat`, `idAgence`, `idVehicule`… Les 400 viennent de `BusinessException` (règle métier) ou d'un JSON illisible ; les 404 de `ResourceNotFoundException`. Le contrôleur ne produit lui-même aucun code d'erreur.

| Contrôleur | Verbe et route | Service appelé | Succès | Erreurs |
| :--- | :--- | :--- | :-: | :-: |
| `ContratController` | `GET /api/contrats` | `getAllContrats()` | 200 | — |
| `ContratController` | `GET /api/contrats/{id}` | `getContratById(id)` | 200 | 404 |
| `ContratController` | `POST /api/contrats` | `createContrat(contrat)` | 201 | 400 |
| `ContratController` | `PUT /api/contrats/{id}` | `updateContrat(id, contrat)` | 200 | 404, 400 |
| `ContratController` | `DELETE /api/contrats/{id}` | `deleteContrat(id)` | 204 | 404 |
| `PaiementController` | `GET /api/paiements` | `getAllPaiements()` | 200 | — |
| `PaiementController` | `GET /api/paiements/{id}` | `getPaiementById(id)` | 200 | 404 |
| `PaiementController` | `POST/PUT/DELETE /api/paiements…` | — (non exposé) | — | 405 |
| `AgenceController` | `GET /api/agences` | `getAllAgences()` | 200 | — |
| `AgenceController` | `GET /api/agences/{id}` | `getAgenceById(id)` | 200 | 404 |
| `AgenceController` | `POST /api/agences` | `createAgence(agence)` | 201 | 400 |
| `AgenceController` | `PUT /api/agences/{id}` | `updateAgence(id, agence)` | 200 | 404, 400 |
| `AgenceController` | `DELETE /api/agences/{id}` | `deleteAgence(id)` | 204 | 404 |
| `EmployeController` | `GET /api/employes` | `getAllEmployes()` | 200 | — |
| `EmployeController` | `GET /api/employes/{id}` | `getEmployeById(id)` | 200 | 404 |
| `EmployeController` | `POST /api/employes` | `createEmploye(employe)` | 201 | 400 |
| `EmployeController` | `PUT /api/employes/{id}` | `updateEmploye(id, employe)` | 200 | 404, 400 |
| `EmployeController` | `DELETE /api/employes/{id}` | `deleteEmploye(id)` | 204 | 404 |
| `VehiculeController` | `GET /api/vehicules` | `getAllVehicules()` | 200 | — |
| `VehiculeController` | `GET /api/vehicules/{id}` | `getVehiculeById(id)` | 200 | 404 |
| `VehiculeController` | `POST /api/vehicules` | `createVehicule(vehicule)` | 201 | 400 |
| `VehiculeController` | `PUT /api/vehicules/{id}` | `updateVehicule(id, vehicule)` | 200 | 404, 400 |
| `VehiculeController` | `DELETE /api/vehicules/{id}` | `deleteVehicule(id)` | 204 | 404 |
| `EquipementController` | `GET /api/equipements` | `getAllEquipements()` | 200 | — |
| `EquipementController` | `GET /api/equipements/{id}` | `getEquipementById(id)` | 200 | 404 |
| `EquipementController` | `POST /api/equipements` | `createEquipement(equipement)` | 201 | 400 |
| `EquipementController` | `PUT /api/equipements/{id}` | `updateEquipement(id, equipement)` | 200 | 404, 400 |
| `EquipementController` | `DELETE /api/equipements/{id}` | `deleteEquipement(id)` | 204 | 404 |
| `ClientController` | `GET /api/clients` | `getAllClients()` | 200 | — |
| `ClientController` | `GET /api/clients/{id}` | `getClientById(id)` | 200 | 404 |
| `ClientController` | `POST /api/clients` | `createClient(client)` | 201 | 400 |
| `ClientController` | `PUT /api/clients/{id}` | `updateClient(id, client)` | 200 | 404, 400 |
| `ClientController` | `DELETE /api/clients/{id}` | `deleteClient(id)` | 204 | 404 |
| `ReservationController` | `GET /api/reservations` | `getAllReservations()` | 200 | — |
| `ReservationController` | `GET /api/reservations/{id}` | `getReservationById(id)` | 200 | 404 |
| `ReservationController` | `POST /api/reservations` | `createReservation(reservation)` | 201 | 400 |
| `ReservationController` | `PUT /api/reservations/{id}` | `updateReservation(id, reservation)` | 200 | 404, 400 |
| `ReservationController` | `DELETE /api/reservations/{id}` | `deleteReservation(id)` | 204 | 404 |
| `MaintenanceController` | `GET /api/maintenances` | `getAllMaintenances()` | 200 | — |
| `MaintenanceController` | `GET /api/maintenances/{id}` | `getMaintenanceById(id)` | 200 | 404 |
| `MaintenanceController` | `POST /api/maintenances` | `createMaintenance(maintenance)` | 201 | 400, 404 (véhicule) |
| `MaintenanceController` | `PUT /api/maintenances/{id}` | `updateMaintenance(id, maintenance)` | 200 | 404, 400 |
| `MaintenanceController` | `DELETE /api/maintenances/{id}` | `deleteMaintenance(id)` | 204 | 404 |

Règles 400 testées dans Postman :

| Entité | Cas 400 |
| :--- | :--- |
| Contrat | identifiant fourni à la création (`"idContrat": 5`), montant total négatif, date mal formée |
| Agence | attribut obligatoire vide (`"nom": ""`), identifiant fourni |
| Employé | rôle inconnu (`"role": "DIRECTEUR"` : JSON illisible) |
| Véhicule | tarif journalier négatif (création et modification) |
| Équipement | libellé vide (création et modification) |
| Client | email vide, email déjà utilisé |
| Réservation | date de fin antérieure à la date de début (création et modification) |
| Maintenance | date de fin antérieure à la date de début (création et modification) |

Remarque : le modèle ne contient ni `quantite` (Équipement) ni `cout` (Maintenance) ; les règles « quantité négative » et « coût négatif » du sujet ne s'appliquent donc pas.

---

## B. Annotations Jackson

| Association | Annotation retenue | Justification |
| :--- | :--- | :--- |
| `Contrat.paiements` ↔ `Paiement.contrat` | `@JsonManagedReference("contrat-paiements")` sur la liste, `@JsonBackReference("contrat-paiements")` sur `Paiement.contrat` | Seule vraie composition (`cascade = ALL`, `orphanRemoval = true`) : les paiements sont créés **dans le JSON du contrat**. À la lecture du JSON, Jackson renseigne automatiquement `paiement.contrat` ; à l'écriture, il n'écrit pas le contrat dans chaque paiement, ce qui casse la boucle. |
| `Reservation.contrat` (côté inverse du `@OneToOne`) | `@JsonIgnore` | Le contrat désigne sa réservation (`"reservation": {"idReservation": 1}`) ; la réservation n'a pas besoin de réécrire son contrat. Sans cela : Contrat → Reservation → Contrat → … |
| `Client.reservations` | `@JsonIgnore` | La réservation est créée seule et désigne son client par identifiant. On garde `Reservation.client` sans annotation : un `@JsonBackReference` y serait ignoré à la lecture et le POST créerait une réservation sans client. |
| `Vehicule.reservations`, `Vehicule.maintenances` | `@JsonIgnore` | Enfants créés seuls (`"vehicule": {"idVehicule": 1}`). |
| `Agence.employes`, `Agence.vehicules` | `@JsonIgnore` | Enfants créés seuls (`"agence": {"idAgence": 1}`). |
| `Equipement.vehicules` (côté inverse du `@ManyToMany`) | `@JsonIgnore` | Le véhicule (propriétaire) expose ses `equipements` ; l'inverse est ignoré pour éviter Vehicule → Equipement → Vehicule → … |

**Différence entre `@JsonBackReference` et `@JsonIgnore`.**
- `@JsonIgnore` ignore la propriété dans les deux sens : elle n'est ni écrite dans la réponse, ni lue depuis la requête.
- `@JsonBackReference` fonctionne en paire avec un `@JsonManagedReference` du même nom : la propriété n'est pas écrite dans la réponse, mais lors de la désérialisation du parent, Jackson la renseigne avec le parent (chaque `Paiement` reçoit son `Contrat`). Elle n'a de sens que si l'enfant est lu **à l'intérieur** du JSON de son parent ; seule, elle n'est jamais lue (d'où le piège sur `Reservation.client`).

---

## C. Postman et Swagger

| Outil | Ce qu'il apporte | Exemple concret rencontré |
| :--- | :--- | :--- |
| **Postman** | Tests rejouables et vérifiés automatiquement (scripts `pm.test`), enchaînement de requêtes grâce aux variables d'environnement (`{{baseUrl}}`, `{{contratId}}`), scénarios multi-étapes et cas d'erreur. | Scénario de cascade : `POST /contrats` avec deux paiements → l'identifiant du premier paiement est mémorisé (`paiementId`) → `GET /paiements` le contient sans création séparée → `PUT /contrats/{id}` sans paiements les conserve → `DELETE /contrats/{id}` → `GET /paiements/{{paiementId}}` répond 404 (cascade REMOVE). La collection complète (102 requêtes, 188 assertions) passe sans échec et peut être rejouée (valeurs uniques générées par un script de collection). |
| **Swagger UI** | Documentation générée depuis le code (toujours synchronisée), visible sans outil externe, essai direct des endpoints (« Try it out »), schémas des entités. | Les endpoints de `ContratController` et `PaiementController` sont apparus dès l'ajout de la dépendance, sans aucune annotation ; la section *Schemas* montre que `Contrat` contient `paiements` mais que `Paiement` ne contient pas `contrat` (effet du `@JsonBackReference`). `PaiementController` n'affiche que deux `GET`, ce qui documente la lecture seule. |

Les deux sont complémentaires : Postman **teste** (vérifie les réponses), Swagger **documente et explore** (contrat de l'API).

---

## D. Anomalies SonarQube for IDE

| # | Règle | Explication | Correction |
| :-: | :--- | :--- | :--- |
| 1 | `java:S1172` — Unused method parameters should be removed | Dans `GlobalExceptionHandler`, `handleUnreadable(HttpMessageNotReadableException ex)` et `handleIntegrity(DataIntegrityViolationException ex)` recevaient l'exception sans l'utiliser. | Paramètre supprimé : Spring choisit le gestionnaire grâce à `@ExceptionHandler(...)`, le paramètre est facultatif. |
| 2 | `java:S4684` — Persistent entities should not be used as arguments of `@RequestMapping` methods | Les méthodes `create` / `update` reçoivent directement une entité JPA en `@RequestBody` : un client peut renseigner des champs internes (identifiant, associations). | Atténuation : les services refusent un identifiant fourni à la création (400) et `update()` ne recopie que les attributs modifiables. Correction complète reportée au Chapitre 10 (DTO), conformément au sujet. |

Bonne pratique respectée (pas d'anomalie `java:S6813` *Field dependency injection should be avoided*) : tous les contrôleurs utilisent un champ `private final I...Service` injecté par constructeur (`@RequiredArgsConstructor`).

> À vérifier dans IntelliJ : clic droit sur `web.controller` > *Analyze with SonarQube for IDE*, puis ajuster ce tableau aux anomalies effectivement signalées.

---

## E. Questions de compréhension

**1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?**
Le `DispatcherServlet` (front controller de Spring MVC). Il interroge le `RequestMappingHandlerMapping`, qui a indexé au démarrage toutes les méthodes annotées `@RequestMapping` / `@GetMapping`… (chemin de classe + chemin de méthode + verbe HTTP). Le `HandlerAdapter` appelle ensuite la méthode trouvée en résolvant ses arguments (`@PathVariable`, `@RequestBody` via Jackson), et la valeur retournée est convertie en JSON par le `HttpMessageConverter` (grâce à `@RestController`).

**2. Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de `ContratServiceImpl` ?**
Pas le repository : le contrôleur doit rester mince et ne pas contenir de logique métier ni d'accès aux données ; les règles (montant négatif, identifiant fourni…) et les transactions sont dans le service. Pas l'implémentation : dépendre de l'interface découple les couches (principe d'inversion des dépendances) ; on peut changer d'implémentation ou la simuler (mock) sans toucher au contrôleur, et Spring injecte le bean `@Service` qui l'implémente (le proxy transactionnel).

**3. Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?**
201 Created indique qu'une nouvelle ressource a été créée (le corps contient la ressource et son identifiant généré). 204 No Content indique un succès sans corps de réponse : après suppression, il n'y a plus rien à renvoyer. Une ressource absente est une erreur du client (il demande un identifiant qui n'existe pas), pas une panne du serveur : le service lève `ResourceNotFoundException`, que le `GlobalExceptionHandler` traduit en 404. 500 est réservé aux erreurs internes imprévues.

**4. Pourquoi `PaiementController` n'expose-t-il que des GET ? Quelle réponse reçoit un POST `/api/paiements` ?**
Le paiement fait partie de la composition du contrat : c'est le `Contrat` qui porte ses paiements (`cascade = ALL`, `orphanRemoval = true`). Les créer, modifier ou supprimer passe donc par le contrat, ce qui garantit qu'un paiement a toujours un contrat. Le contrôleur ne mappe aucune méthode POST sur `/paiements` : Spring répond **405 Method Not Allowed** (vérifié dans Postman, ainsi que pour DELETE).

**5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?**
Risques : couplage fort entre l'API et le schéma de base (renommer une colonne casse les clients), exposition de champs internes, *mass assignment* (le client peut envoyer un identifiant ou des associations), boucles de sérialisation sur les associations bidirectionnelles, `LazyInitializationException` ou proxies Hibernate non sérialisables, et réponses volumineuses. Les DTO (Chapitre 10) règlent ces points.
`Contrat.paiements` est une composition : les paiements sont créés dans le JSON du contrat, il faut donc que Jackson **lise** la liste et relie chaque paiement à son contrat (rôle de la paire `@JsonManagedReference` / `@JsonBackReference`). Les véhicules sont créés seuls et désignent leur agence par identifiant (`"agence": {"idAgence": 1}`) : la liste `Agence.vehicules` n'a besoin d'être ni lue ni écrite, on l'ignore simplement, et `Vehicule.agence` reste sans annotation pour être lue à la création.
