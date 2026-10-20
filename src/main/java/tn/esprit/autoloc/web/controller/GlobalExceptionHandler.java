package tn.esprit.autoloc.web.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

import java.time.LocalDateTime;

/**
 * Traduit les exceptions levées par la couche Service en réponses HTTP JSON lisibles
 * (statut, message, horodatage). Les contrôleurs ne produisent eux-mêmes aucun code d'erreur.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErreurApi(int statut, String message, LocalDateTime horodatage) {
    }

    // Ressource absente -> 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErreurApi> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Règle métier violée -> 400
    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class})
    public ResponseEntity<ErreurApi> handleBadRequest(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // JSON mal formé, date hors format AAAA-MM-JJ, valeur d'énumération invalide -> 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErreurApi> handleUnreadable() {
        return build(HttpStatus.BAD_REQUEST, "Corps de requête JSON illisible ou mal formé");
    }

    // Identifiant de chemin non numérique (ex. /contrats/abc) -> 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErreurApi> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, "Valeur invalide pour le paramètre '" + ex.getName() + "'");
    }

    // Contrainte de base violée (colonne obligatoire, unicité, clé étrangère) -> 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErreurApi> handleIntegrity() {
        return build(HttpStatus.CONFLICT, "Opération refusée : contrainte d'intégrité des données violée");
    }

    private ResponseEntity<ErreurApi> build(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErreurApi(status.value(), message, LocalDateTime.now()));
    }
}
