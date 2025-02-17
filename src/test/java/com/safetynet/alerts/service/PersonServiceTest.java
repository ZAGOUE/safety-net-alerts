package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);

    private PersonService personService;

    @BeforeEach
    void setUp() {
        JsonDataLoader jsonDataLoader = new JsonDataLoader();
        personService = new PersonService(jsonDataLoader);
    }

    @Test
    void testAddPerson() {
        Person newPerson = new Person("Jane", "Doe", "150 Main St", "Culver", "12345", "555-1234", "jane.doe@example.com","03/06/1984");
        boolean added = personService.addPerson(newPerson);
        logger.info("✅ Test ajout : {}", added ? "SUCCÈS" : "ÉCHEC");
        assertTrue(added);
    }

    @Test
    void testAddPersonAlreadyExists() {
        Person existingPerson = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john.boyd@example.com","03/06/1984");
        boolean added = personService.addPerson(existingPerson);
        logger.info("❌ Test ajout d'une personne existante : {}", added ? "ÉCHEC" : "SUCCÈS");
        assertFalse(added);
    }

    @Test
    void testUpdatePerson() {
        Person updatedPerson = new Person("John", "Boyd", "Updated St", "NewCity", "12345", "555-9876", "new.john@example.com", "03/06/1984");
        boolean updated = personService.updatePerson("John", "Boyd", updatedPerson);
        logger.info("✅ Test mise à jour : {}", updated ? "SUCCÈS" : "ÉCHEC");
        assertTrue(updated);
    }

    @Test
    void testUpdateNonExistingPerson() {
        // ✅ Utilisation d'une date valide
        Person updatedPerson = new Person("Ghost", "Person", "Unknown St", "Nowhere", "00000", "000-0000", "ghost@example.com", "01/01/1900");

        // 🔍 On tente de mettre à jour une personne qui n'existe pas
        boolean updated = personService.updatePerson("Ghost", "Person", updatedPerson);

        // ✅ Amélioration des logs
        logger.info("✅ Test mise à jour d'une personne inexistante : {}", updated ? "PROBLÈME ❌" : "SUCCÈS ✅");

        // ✅ Vérification que la mise à jour ne s'est pas faite
        assertFalse(updated);
    }


    @Test
    void testDeletePerson() {
        boolean deleted = personService.deletePerson("John", "Boyd");
        logger.info("✅ Test suppression : {}", deleted ? "SUCCÈS" : "ÉCHEC");
        assertTrue(deleted);
    }

    @Test
    void testDeleteNonExistingPerson() {
        boolean deleted = personService.deletePerson("Ghost", "Person");
        logger.info("❌ Test suppression d'une personne inexistante : {}", deleted ? "ÉCHEC" : "SUCCÈS");
        assertFalse(deleted);
    }

    @Test
    void testGetAllPersons() {
        List<PersonDTO> persons = personService.getAllPersons();

        // Vérification que la liste n'est pas nulle
        assertNotNull(persons, "La liste des personnes ne doit pas être nulle.");

        // Vérification que la liste n'est pas vide
        assertFalse(persons.isEmpty(), "La liste des personnes ne doit pas être vide.");

        // Log seulement si la liste est valide
        logger.info("✅ Test récupération de toutes les personnes : {} personnes trouvées", persons.size());

        // Vérification de la cohérence des données
        assertEquals(persons.size(), personService.getAllPersons().size());
    }

}
