package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class FirestationServiceTest {

    private static final Logger logger = LogManager.getLogger(FirestationServiceTest.class);


    private FirestationService firestationService;

    private JsonDataLoader jsonDataLoader;

    @BeforeEach
    void setUp() throws Exception {
        // 🔁 Restaure l’état initial du fichier JSON avant chaque test
        Files.copy(
                Path.of("src/test/resources/data-backup.json"),
                Path.of("src/main/resources/data.json"),
                StandardCopyOption.REPLACE_EXISTING
        );

      jsonDataLoader = new JsonDataLoader();
        firestationService = new FirestationService(jsonDataLoader);
    }



    @Test
    void testGetAllFirestations() {
        List<Firestation> firestations = firestationService.getAllFirestations();
        assertNotNull(firestations);
        assertFalse(firestations.isEmpty());
    }

    @Test
    void testUpdateFirestation_Success() {
        Firestation updated = new Firestation("1509 Culver St", 99); // l'adresse existe dans le (JSON).

        boolean updatedResult = firestationService.updateFirestation("1509 Culver St", updated);

        logger.info("Test update : {}", updatedResult ? "SUCCÈS" : "ÉCHEC");
        assertTrue(updatedResult, "La mise à jour aurait dû réussir !");
    }
    @Test
    void testUpdateFirestation_Echec() {
        Firestation updated = new Firestation("Adresse inconnue", 2);

        boolean updatedResult = firestationService.updateFirestation("Adresse inconnue", updated);

        logger.info("Test update (échec) : {}", updatedResult ? "SUCCÈS" : "ÉCHEC ATTENDU");
        assertFalse(updatedResult, "La mise à jour aurait dû échouer !");
    }


    @Test
    void testAddFirestation_Success() {
        Firestation newFirestation = new Firestation("10 Paris Street", 6);

        boolean added = firestationService.addFirestation(newFirestation);

        assertTrue(added, "La caserne aurait dû être ajoutée !");
    }

    @Test
    void testDeleteFirestation_Success() {
        boolean deleted = firestationService.deleteFirestation("1509 Culver St"); // existant dans le fichier

        logger.info("Test suppression : {}", deleted ? "SUCCÈS" : "ÉCHEC");
        assertTrue(deleted, "La suppression aurait dû réussir !");
    }
    @Test
    void testDeleteFirestation_Echec() {
        boolean deleted = firestationService.deleteFirestation("Adresse Inexistante");

        logger.info("Test suppression (échec) : {}", deleted ? "SUCCÈS" : "ÉCHEC ATTENDU");
        assertFalse(deleted, "La suppression aurait dû échouer !");
    }



    @Test
    void testGetPersonsByStation() {
        int stationNumber = 1;
        List<Person> mockPersons = List.of(
                new Person("Alice", "Smith", "1509 Culver St", "City", "12345", "555-1234", "email@example.com", "01/01/2000")
        );

        List<Firestation> mockFirestations = List.of(
                new Firestation("1509 Culver St", stationNumber)
        );

        when(jsonDataLoader.getAllPersons()).thenReturn(mockPersons);
        when(jsonDataLoader.getAllFirestations()).thenReturn(mockFirestations);

        Map<String, Object> result = firestationService.getPersonsByStation(stationNumber);

        assertNotNull(result);
        assertTrue(result.containsKey("persons"));
        assertTrue(result.containsKey("adultCount"));
        assertTrue(result.containsKey("childCount"));
    }
    @Test
    void testGetPhoneNumbersByStation() {
        int stationNumber = 1;
        List<Person> mockPersons = List.of(
                new Person("Alice", "Smith", "1509 Culver St", "City", "12345", "555-1234", "email@example.com", "01/01/2000")

        );

        List<Firestation> mockFirestations = List.of(
                new Firestation("1509 Culver St", stationNumber)
        );

        when(jsonDataLoader.getAllPersons()).thenReturn(mockPersons);
        when(jsonDataLoader.getAllFirestations()).thenReturn(mockFirestations);

        List<String> phoneNumbers = firestationService.getPhoneNumbersByStation(stationNumber);

        assertFalse(phoneNumbers.isEmpty(), "Des numéros de téléphone devraient être trouvés !");
    }

    @Test
    void testGetFirestationByAddress() {
        String address = "1509 Culver St";
        Firestation firestation = firestationService.getFirestationByAddress(address);

        assertNotNull(firestation);
        assertEquals(address, firestation.getAddress());
    }
    @Test
    void testGetFloodStations() {
        List<Integer> stations = List.of(1, 2);
        Map<String, List<PersonDTO>> result = firestationService.getFloodStations(stations);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    @Test
    void testGetFireInfoByAddress() {
        String address = "1509 Culver St";
        List<Person> mockPersons = List.of(
                new Person("Alice", "Smith", "1509 Culver St", "City", "12345", "555-1234", "email@example.com", "01/01/2000")

        );

        List<MedicalRecord> mockMedicalRecords = List.of(
                new MedicalRecord("Alice", "Smith", "05/10/1990", List.of("med1"), List.of("allergy1"))

        );

        List<Firestation> mockFirestations = List.of(
                new Firestation(address, 1)
        );

        when(jsonDataLoader.getAllPersons()).thenReturn(mockPersons);
        when(jsonDataLoader.getAllMedicalRecords()).thenReturn(mockMedicalRecords);
        when(jsonDataLoader.getAllFirestations()).thenReturn(mockFirestations);

        FireDTO fireInfo = firestationService.getFireInfoByAddress(address);

        assertNotNull(fireInfo);
        assertFalse(fireInfo.getResidents().isEmpty(), "Il devrait y avoir des habitants !");
    }

    @Test
    void testGetChildrenByAddress() {
        String address = "1509 Culver St";
        List<Person> mockPersons = List.of(
                new Person("Alice", "Smith", "1509 Culver St", "City", "12345", "555-1234", "email@example.com", "01/01/2000")

        );

        List<MedicalRecord> mockMedicalRecords = List.of(
                new MedicalRecord("Alice", "Smith", "08/15/2015", List.of("med1"), List.of("allergy1"))
        );


        when(jsonDataLoader.getAllPersons()).thenReturn(mockPersons);
        when(jsonDataLoader.getAllMedicalRecords()).thenReturn(mockMedicalRecords);

        Map<String, Object> result = firestationService.getChildrenByAddress(address);

        assertTrue(result.containsKey("children"));
        assertFalse(((List<?>) result.get("children")).isEmpty(), "Il devrait y avoir au moins un enfant !");
    }







}
