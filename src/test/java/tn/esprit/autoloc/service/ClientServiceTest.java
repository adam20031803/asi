package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClientServiceTest {

    @Autowired
    private IClientService clientService;

    @Autowired
    private IClientRepository clientRepository;

    private Client clientTest;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();

        clientTest = new Client();
        clientTest.setNom("Ben Salem");
        clientTest.setPrenom("Youssef");
        clientTest.setEmail("youssef.bensalem@esprit.tn");
        clientTest.setTelephone("+21698765432");
        clientTest.setNumPermis("TN-987654321");
        clientTest.setDateInscription(LocalDate.now());
    }

    @Test
    @DisplayName("Test Create Client - Succès")
    void testCreateClient_Success() {
        Client saved = clientService.createClient(clientTest);

        assertNotNull(saved.getIdClient());
        assertEquals("Ben Salem", saved.getNom());
        assertEquals("youssef.bensalem@esprit.tn", saved.getEmail());
    }

    @Test
    @DisplayName("Test Create Client - Email en double (Exception)")
    void testCreateClient_DuplicateEmail_ThrowsException() {
        clientService.createClient(clientTest);

        Client duplicate = new Client();
        duplicate.setNom("Autre");
        duplicate.setPrenom("Client");
        duplicate.setEmail("youssef.bensalem@esprit.tn");
        duplicate.setTelephone("+21611111111");
        duplicate.setNumPermis("TN-000000000");

        assertThrows(BusinessException.class, () -> clientService.createClient(duplicate));
    }

    @Test
    @DisplayName("Test Get Client By Id - Succès")
    void testGetClientById_Success() {
        Client saved = clientService.createClient(clientTest);
        Client retrieved = clientService.getClientById(saved.getIdClient());

        assertNotNull(retrieved);
        assertEquals(saved.getIdClient(), retrieved.getIdClient());
        assertEquals("Youssef", retrieved.getPrenom());
    }

    @Test
    @DisplayName("Test Get Client By Id - Non Trouvé (Exception)")
    void testGetClientById_NotFound_ThrowsException() {
        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(9999L));
    }

    @Test
    @DisplayName("Test Update Client - Succès")
    void testUpdateClient_Success() {
        Client saved = clientService.createClient(clientTest);

        Client updateData = new Client();
        updateData.setNom("Ben Salem Modifié");
        updateData.setTelephone("+21622222222");

        Client updated = clientService.updateClient(saved.getIdClient(), updateData);

        assertEquals("Ben Salem Modifié", updated.getNom());
        assertEquals("+21622222222", updated.getTelephone());
        assertEquals("Youssef", updated.getPrenom()); // Inchangé
    }

    @Test
    @DisplayName("Test Delete Client - Succès")
    void testDeleteClient_Success() {
        Client saved = clientService.createClient(clientTest);
        Long id = saved.getIdClient();

        clientService.deleteClient(id);

        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(id));
    }

    @Test
    @DisplayName("Test Get All Clients - Succès")
    void testGetAllClients_Success() {
        clientService.createClient(clientTest);

        Client client2 = new Client();
        client2.setNom("Trabelsi");
        client2.setPrenom("Salma");
        client2.setEmail("salma.trabelsi@esprit.tn");
        client2.setTelephone("+21655555555");
        client2.setNumPermis("TN-555555555");
        clientService.createClient(client2);

        List<Client> list = clientService.getAllClients();
        assertEquals(2, list.size());
    }
}
