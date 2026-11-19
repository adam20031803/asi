package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Client;

import java.util.List;
import java.util.Optional;

public interface IClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    Optional<Client> findByNumPermis(String numPermis);

    boolean existsByEmail(String email);

    boolean existsByNumPermis(String numPermis);

    /** Recherche par nom ET prénom (insensible à la casse). */
    List<Client> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
}
