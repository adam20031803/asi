package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe createEmploye(Employe employe) {
        if (employe == null) {
            throw new BusinessException("Les données de l'employé ne peuvent pas être nulles");
        }
        if (employe.getIdEmploye() != null) {
            throw new BusinessException("L'identifiant de l'employé est généré automatiquement : il ne doit pas être fourni à la création");
        }
        return employeRepository.save(employe);
    }

    @Override
    public Employe updateEmploye(Long id, Employe updatedEmploye) {
        if (updatedEmploye == null) {
            throw new BusinessException("Les données de mise à jour de l'employé ne peuvent pas être nulles");
        }
        Employe existingEmploye = getEmployeById(id);

        if (updatedEmploye.getNom() != null) {
            existingEmploye.setNom(updatedEmploye.getNom());
        }
        if (updatedEmploye.getPrenom() != null) {
            existingEmploye.setPrenom(updatedEmploye.getPrenom());
        }
        if (updatedEmploye.getRole() != null) {
            existingEmploye.setRole(updatedEmploye.getRole());
        }
        if (updatedEmploye.getAgence() != null) {
            existingEmploye.setAgence(updatedEmploye.getAgence());
        }

        return employeRepository.save(existingEmploye);
    }

    @Override
    @Transactional(readOnly = true)
    public Employe getEmployeById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    @Override
    public void deleteEmploye(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : aucun employé avec l'identifiant : " + id);
        }
        employeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> getEmployesByAgence(Long idAgence) {
        return employeRepository.findByAgence_IdAgence(idAgence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> getEmployesByRole(RoleEmploye role) {
        return employeRepository.findByRole(role);
    }
}
