package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Contrat;

import java.util.List;
import java.util.Optional;

public interface IContratRepository extends JpaRepository<Contrat, Long> {

    Optional<Contrat> findByReservation_IdReservation(Long idReservation);

    List<Contrat> findByValide(boolean valide);
}