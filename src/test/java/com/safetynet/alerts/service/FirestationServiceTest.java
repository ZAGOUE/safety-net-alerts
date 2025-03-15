package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FirestationServiceTest {
    private final FirestationService firestationService = new FirestationService(new JsonDataLoader());

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
        Firestation newStation = new Firestation("123 New St", 10);
        boolean result = firestationService.addFirestation(newStation);

        assertTrue(result, "L'ajout de la caserne aurait dû réussir !");
        List<Firestation> stations = firestationService.getAllFirestations();
        assertTrue(stations.contains(newStation), "La caserne ajoutée est introuvable !");
    }
    @Test
    void testDeleteFirestation_Success() {
        boolean result = firestationService.deleteFirestation("1509 Culver St");
        assertTrue(result, "La suppression aurait dû réussir !");
        Firestation station = firestationService.findByAddress("1509 Culver St");
        assertNull(station, "La caserne supprimée est toujours présente !");
    }
}
