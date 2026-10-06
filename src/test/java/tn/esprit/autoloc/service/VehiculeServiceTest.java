package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VehiculeServiceTest {

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Autowired
    private IEquipementRepository equipementRepository;

    @Autowired
    private IAgenceRepository agenceRepository;

    private Vehicule vehiculeTest;
    private Agence agenceTest;
    private Equipement equipementTest;

    @BeforeEach
    void setUp() {
        vehiculeRepository.deleteAll();

        agenceTest = new Agence();
        agenceTest.setNom("AutoLoc Tunis Centre");
        agenceTest.setVille("Tunis");
        agenceTest.setAdresse("Avenue Habib Bourguiba");
        agenceTest.setTelephone("+21671000000");
        agenceTest = agenceRepository.save(agenceTest);

        equipementTest = new Equipement();
        equipementTest.setLibelle("GPS Navigation HD");
        equipementTest = equipementRepository.save(equipementTest);

        vehiculeTest = new Vehicule();
        vehiculeTest.setImmatriculation("234-TN-5678");
        vehiculeTest.setMarque("Volkswagen");
        vehiculeTest.setModele("Golf 8");
        vehiculeTest.setCategorie(CategorieVehicule.BERLINE);
        vehiculeTest.setTarifJournalier(new BigDecimal("120.00"));
        vehiculeTest.setStatut(StatutVehicule.DISPONIBLE);
        vehiculeTest.setAgence(agenceTest);
    }

    @Test
    @DisplayName("Test Create Vehicule - Succès")
    void testCreateVehicule_Success() {
        Vehicule saved = vehiculeService.createVehicule(vehiculeTest);

        assertNotNull(saved.getIdVehicule());
        assertEquals("234-TN-5678", saved.getImmatriculation());
        assertEquals("Volkswagen", saved.getMarque());
        assertEquals(StatutVehicule.DISPONIBLE, saved.getStatut());
    }

    @Test
    @DisplayName("Test Create Vehicule - Immatriculation en double (Exception)")
    void testCreateVehicule_DuplicateImmatriculation_ThrowsException() {
        vehiculeService.createVehicule(vehiculeTest);

        Vehicule duplicate = new Vehicule();
        duplicate.setImmatriculation("234-TN-5678");
        duplicate.setMarque("Renault");
        duplicate.setModele("Clio");
        duplicate.setCategorie(CategorieVehicule.CITADINE);
        duplicate.setTarifJournalier(new BigDecimal("80.00"));

        assertThrows(BusinessException.class, () -> vehiculeService.createVehicule(duplicate));
    }

    @Test
    @DisplayName("Test Create Vehicule - Tarif négatif ou nul (Exception)")
    void testCreateVehicule_InvalidTarif_ThrowsException() {
        vehiculeTest.setTarifJournalier(new BigDecimal("-10.00"));
        assertThrows(BusinessException.class, () -> vehiculeService.createVehicule(vehiculeTest));
    }

    @Test
    @DisplayName("Test Get Vehicule By Id - Succès")
    void testGetVehiculeById_Success() {
        Vehicule saved = vehiculeService.createVehicule(vehiculeTest);
        Vehicule retrieved = vehiculeService.getVehiculeById(saved.getIdVehicule());

        assertNotNull(retrieved);
        assertEquals("Golf 8", retrieved.getModele());
    }

    @Test
    @DisplayName("Test Update Vehicule - Succès")
    void testUpdateVehicule_Success() {
        Vehicule saved = vehiculeService.createVehicule(vehiculeTest);

        Vehicule updateData = new Vehicule();
        updateData.setTarifJournalier(new BigDecimal("140.00"));
        updateData.setStatut(StatutVehicule.MAINTENANCE);

        Vehicule updated = vehiculeService.updateVehicule(saved.getIdVehicule(), updateData);

        assertEquals(new BigDecimal("140.00"), updated.getTarifJournalier());
        assertEquals(StatutVehicule.MAINTENANCE, updated.getStatut());
        assertEquals("Volkswagen", updated.getMarque()); // Inchangé
    }

    @Test
    @DisplayName("Test Delete Vehicule - Succès")
    void testDeleteVehicule_Success() {
        Vehicule saved = vehiculeService.createVehicule(vehiculeTest);
        Long id = saved.getIdVehicule();

        vehiculeService.deleteVehicule(id);

        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.getVehiculeById(id));
    }

    @Test
    @DisplayName("Test Changer Statut Vehicule - Succès")
    void testChangerStatut_Success() {
        Vehicule saved = vehiculeService.createVehicule(vehiculeTest);
        Vehicule updated = vehiculeService.changerStatut(saved.getIdVehicule(), StatutVehicule.LOUE);

        assertEquals(StatutVehicule.LOUE, updated.getStatut());
    }

    @Test
    @DisplayName("Test Affecter et Retirer Equipement - Succès")
    void testAffecterEtRetirerEquipement_Success() {
        Vehicule saved = vehiculeService.createVehicule(vehiculeTest);

        Vehicule withEquip = vehiculeService.affecterEquipement(saved.getIdVehicule(), equipementTest.getIdEquipement());
        assertEquals(1, withEquip.getEquipements().size());
        assertEquals("GPS Navigation HD", withEquip.getEquipements().get(0).getLibelle());

        Vehicule withoutEquip = vehiculeService.retirerEquipement(saved.getIdVehicule(), equipementTest.getIdEquipement());
        assertEquals(0, withoutEquip.getEquipements().size());
    }
}
