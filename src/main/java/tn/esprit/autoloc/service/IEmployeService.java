package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;

import java.util.List;

public interface IEmployeService {

    Employe createEmploye(Employe employe);

    Employe updateEmploye(Long id, Employe employe);

    Employe getEmployeById(Long id);

    List<Employe> getAllEmployes();

    void deleteEmploye(Long id);

    List<Employe> getEmployesByAgence(Long idAgence);

    List<Employe> getEmployesByRole(RoleEmploye role);
}
