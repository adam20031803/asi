package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Agence;

import java.util.List;

public interface IAgenceService {

    Agence createAgence(Agence agence);

    Agence updateAgence(Long id, Agence agence);

    Agence getAgenceById(Long id);

    List<Agence> getAllAgences();

    void deleteAgence(Long id);

    List<Agence> getAgencesByVille(String ville);
}
