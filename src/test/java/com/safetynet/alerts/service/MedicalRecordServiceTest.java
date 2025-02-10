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
        MedicalRecord updatedRecord = new MedicalRecord("John", "Boyd", "03/06/1984",
                List.of("med1:500mg", "med2:250mg"),
                List.of("pollen", "nuts"));

        assertTrue(medicalRecordService.updateMedicalRecord("John", "Boyd", updatedRecord));
    }


    @Test
    void testDeleteMedicalRecord() {
        assertTrue(medicalRecordService.deleteMedicalRecord("John", "Boyd"));
    }
}

