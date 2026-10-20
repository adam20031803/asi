package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IAgenceService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public Agence createAgence(Agence agence) {
        if (agence == null) {
            throw new BusinessException("Les informations de l'agence ne peuvent pas être nulles");
        }
        if (agence.getIdAgence() != null) {
            throw new BusinessException("L'identifiant de l'agence est généré automatiquement : il ne doit pas être fourni à la création");
        }
        if (isBlank(agence.getNom()) || isBlank(agence.getVille())
                || isBlank(agence.getAdresse()) || isBlank(agence.getTelephone())) {
            throw new BusinessException("Le nom, la ville, l'adresse et le téléphone de l'agence sont obligatoires");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public Agence updateAgence(Long id, Agence updatedAgence) {
        if (updatedAgence == null) {
            throw new BusinessException("Les données de mise à jour de l'agence ne peuvent pas être nulles");
        }
        Agence existingAgence = getAgenceById(id);

        if (updatedAgence.getNom() != null) {
            existingAgence.setNom(updatedAgence.getNom());
        }
        if (updatedAgence.getVille() != null) {
            existingAgence.setVille(updatedAgence.getVille());
        }
        if (updatedAgence.getAdresse() != null) {
            existingAgence.setAdresse(updatedAgence.getAdresse());
        }
        if (updatedAgence.getTelephone() != null) {
            existingAgence.setTelephone(updatedAgence.getTelephone());
        }

        return agenceRepository.save(existingAgence);
    }

    @Override
    @Transactional(readOnly = true)
    public Agence getAgenceById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agence> getAllAgences() {
        return agenceRepository.findAll();
    }

    @Override
    public void deleteAgence(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : aucune agence avec l'identifiant : " + id);
        }
        agenceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agence> getAgencesByVille(String ville) {
        return agenceRepository.findByVilleIgnoreCase(ville);
    }

    private static boolean isBlank(String valeur) {
        return valeur == null || valeur.isBlank();
    }
}
