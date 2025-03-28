package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FirestationServiceTest {
    @InjectMocks
    private FirestationService firestationService;

    @Mock
    private JsonDataLoader jsonDataLoader;

    @BeforeEach
    void setUp() {
        List<Firestation> mockFirestations = List.of(
                new Firestation("1509 Culver St", 3),
                new Firestation("29 15th St", 2)
        );

        Mockito.lenient().when(jsonDataLoader.getAllFirestations()).thenReturn(mockFirestations);
    }


    @Test
    void testGetAllFirestations() {
        List<Firestation> firestations = firestationService.getAllFirestations();
        assertNotNull(firestations);
        assertFalse(firestations.isEmpty());
    }

    @Test
    void testUpdateFirestation() {
        Firestation updatefirestation = new Firestation("1509 Culver St", 5); // Crée un objet Firestation
        assertTrue(firestationService.updateFirestation("1509 Culver St", updatefirestation)); // Utilise l'objet
    }
    @Test
    void testAddFirestation_Success() {
        Firestation newFirestation = new Firestation("10 New Street", 3);

        when(jsonDataLoader.getAllFirestations()).thenReturn(new ArrayList<>()); // Simulation d'une liste vide initialement

        boolean added = firestationService.addFirestation(newFirestation);

        assertTrue(added, "La caserne aurait dû être ajoutée !");
    }

    @Test
    void testDeleteFirestation_Success() {
        Firestation existingFirestation = new Firestation("1509 Culver St", 1);
        List<Firestation> initialList = new ArrayList<>(List.of(existingFirestation));

        when(jsonDataLoader.getAllFirestations()).thenReturn(new ArrayList<>(List.of(existingFirestation)));


        boolean deleted = firestationService.deleteFirestation("1509 Culver St");

        assertTrue(deleted, "La suppression aurait dû réussir !");
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
                new MedicalRecord("Alice", "Smith", "08/15/2015", List.of("med1"), List.of("allergy1")) // Moins de 18 ans
        );


        when(jsonDataLoader.getAllPersons()).thenReturn(mockPersons);
        when(jsonDataLoader.getAllMedicalRecords()).thenReturn(mockMedicalRecords);

        Map<String, Object> result = firestationService.getChildrenByAddress(address);

        assertTrue(result.containsKey("children"));
        assertFalse(((List<?>) result.get("children")).isEmpty(), "Il devrait y avoir au moins un enfant !");
    }







}
