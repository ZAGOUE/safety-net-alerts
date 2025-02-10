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
    void testAddFirestation() {
        Firestation firestation = new Firestation("999 New Ave", 6);
     assertTrue(firestationService.addFirestation(firestation));
  }

    @Test
    void testUpdateFirestation() {
        Firestation updatefirestation = new Firestation("1509 Culver St", 5); // Crée un objet Firestation
        assertTrue(firestationService.updateFirestation("1509 Culver St", updatefirestation)); // Utilise l'objet
    }

    @Test
    void testDeleteFirestation() {
        assertTrue(firestationService.deleteFirestation("1509 Culver St"));
    }
}
