package tn.esprit.autoloc.web.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestionnaire centralisé des erreurs et exceptions pour l'ensemble des contrôleurs.
 * Traduit les exceptions en réponses HTTP JSON structurées (statut, message, horodatage).
 * Les contrôleurs ne produisent eux-mêmes aucun code d'erreur.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // -------------------------------------------------------------------------
    // Enveloppe de réponse standard pour les erreurs simples
    // -------------------------------------------------------------------------
    public record ErreurApi(int statut, String message, LocalDateTime horodatage) {
    }

    // -------------------------------------------------------------------------
    // Enveloppe étendue pour les erreurs de validation (liste des violations)
    // -------------------------------------------------------------------------
    public record ErreurValidation(
            int statut,
            String message,
            Map<String, String> erreurs,
            LocalDateTime horodatage) {
    }

    // =========================================================================
    // 1. Erreurs de validation Bean (@Valid sur les DTO Request) → 422
    // =========================================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErreurValidation> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erreurs = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Valeur invalide",
                        (msg1, msg2) -> msg1,       // en cas de doublons, garder le premier
                        LinkedHashMap::new));

        ErreurValidation body = new ErreurValidation(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation des données échouée : " + erreurs.size() + " champ(s) invalide(s)",
                erreurs,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    // =========================================================================
    // 2. Ressource absente → 404
    // =========================================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErreurApi> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // =========================================================================
    // 3. Règle métier violée → 400
    // =========================================================================
    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class})
    public ResponseEntity<ErreurApi> handleBadRequest(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // =========================================================================
    // 4. JSON mal formé, date hors format AAAA-MM-JJ, valeur d'énumération invalide → 400
    // =========================================================================
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErreurApi> handleUnreadable() {
        return build(HttpStatus.BAD_REQUEST, "Corps de requête JSON illisible ou mal formé");
    }

    // =========================================================================
    // 5. Identifiant de chemin non numérique (ex. /contrats/abc) → 400
    // =========================================================================
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErreurApi> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST,
                "Valeur invalide pour le paramètre '" + ex.getName() + "'");
    }

    // =========================================================================
    // 6. Contrainte de base violée (unicité, clé étrangère, NOT NULL) → 409
    // =========================================================================
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErreurApi> handleIntegrity() {
        return build(HttpStatus.CONFLICT,
                "Opération refusée : contrainte d'intégrité des données violée");
    }

    // =========================================================================
    // 7. Toute autre exception non traitée → 500
    // =========================================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurApi> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur interne est survenue. Veuillez réessayer ultérieurement.");
    }

    // -------------------------------------------------------------------------
    // Méthode utilitaire
    // -------------------------------------------------------------------------
    private ResponseEntity<ErreurApi> build(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErreurApi(status.value(), message, LocalDateTime.now()));
    }
}
