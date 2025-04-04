package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);
    private PersonService personService;

    @BeforeEach
    void setUp() throws Exception {
        // 🔁 Restaure l’état initial du fichier JSON avant chaque test
        Files.copy(
                Path.of("src/test/resources/data-backup.json"),
                Path.of("src/main/resources/data.json"),
                StandardCopyOption.REPLACE_EXISTING
        );

        JsonDataLoader jsonDataLoader = new JsonDataLoader();
        personService = new PersonService(jsonDataLoader);
    }

    @Test
    void testAddPerson() {
        Person newPerson = new Person("Doudou", "Doe", "150 Main St", "Culver", "12345", "555-1234", "doudoudoe@habitants.com", "03/06/1984");
        boolean added = personService.addPerson(newPerson);
        logger.info("Test ajout : {}", added ? "SUCCÈS" : "ÉCHEC");
        assertTrue(added);
    }

    @Test
    void testAddPersonAlreadyExists() {
        Person existingPerson = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john.boyd@example.com", "03/06/1984");
        boolean added = personService.addPerson(existingPerson);
        logger.info("Test ajout d'une personne existante : {}", added ? "ÉCHEC" : "SUCCÈS");
        assertFalse(added);
    }

    @Test
    void testUpdatePerson() {
        Person updatedPerson = new Person("John", "Boyd", "Updated St", "NewCity", "12345", "555-9876", "new.habitants.com", "03/06/1984");
        boolean updated = personService.updatePerson("John", "Boyd", updatedPerson);
        logger.info("Test mise à jour : {}", updated ? "SUCCÈS" : "ÉCHEC");
        assertTrue(updated);
    }

    @Test
    void testUpdateNonExistingPerson() {
        Person updatedPerson = new Person("Ghost", "Person", "Unknown St", "Nowhere", "00000", "000-0000", "ghost@example.com", "01/01/1900");
        boolean updated = personService.updatePerson("Ghost", "Person", updatedPerson);
        logger.info("Test mise à jour d'une personne inexistante : {}", updated ? "PROBLÈME " : "SUCCÈS ");
        assertFalse(updated);
    }

    @Test
    void testDeletePerson() {
        boolean deleted = personService.deletePerson("John", "Boyd");
        logger.info("Test suppression : {}", deleted ? "SUCCÈS" : "ÉCHEC");
        assertTrue(deleted);
    }

    @Test
    void testDeleteNonExistingPerson() {
        boolean deleted = personService.deletePerson("Ghost", "Person");
        logger.info("Test suppression d'une personne inexistante : {}", deleted ? "ÉCHEC" : "SUCCÈS");
        assertFalse(deleted);
    }

    @Test
    void testGetAllPersons() {
        List<PersonDTO> persons = personService.getAllPersons();
        assertNotNull(persons);
        assertFalse(persons.isEmpty());
        logger.info("Test récupération de toutes les personnes : {} personnes trouvées", persons.size());
    }

    @Test
    void testGetPersonInfoByLastName() {
        List<PersonInfoDTO> infos = personService.getPersonInfoByLastName("Boyd");
        assertFalse(infos.isEmpty());
        assertEquals("Boyd", infos.get(0).getLastName());
    }

    @Test
    void testGetCommunityEmails() {
        Set<String> emails = personService.getCommunityEmails("Culver");
        assertNotNull(emails);
        assertTrue(emails.contains("jaboyd@email.com"));
    }

    @Test
    void testCalculateAge() {
        int age = personService.calculateAge("01/01/2000");
        assertTrue(age > 0);
    }
}
