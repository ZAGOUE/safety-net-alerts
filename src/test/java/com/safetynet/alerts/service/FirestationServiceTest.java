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
        Firestation firestation = new Firestation("150 Main St", 5);
        assertTrue(firestationService.addFirestation(firestation));
    }

    @Test
    void testUpdateFirestation() {
        assertTrue(firestationService.updateFirestation("150 Main St", 7));
    }

    @Test
    void testDeleteFirestation() {
        assertTrue(firestationService.deleteFirestation("150 Main St"));
    }
}
