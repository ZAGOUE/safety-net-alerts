package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordServiceTest {
    private final MedicalRecordService medicalRecordService = new MedicalRecordService(new JsonDataLoader());

    @Test
    void testGetAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();
        assertNotNull(medicalRecords);
        assertFalse(medicalRecords.isEmpty());
    }

    @Test
    void testAddMedicalRecord() {
        MedicalRecord record = new MedicalRecord("Jane", "Doe", "01/01/1990", List.of("Aspirin"), List.of("Peanuts"));
        assertTrue(medicalRecordService.addMedicalRecord(record));
    }

    @Test
    void testUpdateMedicalRecord() {
        MedicalRecord record = new MedicalRecord("Jane", "Doe", "01/01/1990", List.of("Ibuprofen"), List.of("Peanuts"));
        assertTrue(medicalRecordService.updateMedicalRecord("Jane", "Doe", record));
    }

    @Test
    void testDeleteMedicalRecord() {
        assertTrue(medicalRecordService.deleteMedicalRecord("Jane", "Doe"));
    }
}

