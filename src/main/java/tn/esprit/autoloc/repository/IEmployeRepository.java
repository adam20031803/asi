package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;

import java.util.List;

public interface IEmployeRepository extends JpaRepository<Employe, Long> {

    List<Employe> findByAgence_IdAgence(Long idAgence);

    List<Employe> findByRole(RoleEmploye role);
}
