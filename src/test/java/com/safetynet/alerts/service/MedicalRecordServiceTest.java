package com.safetynet.alerts.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.BeforeEach;


import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordServiceTest {

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(
                Path.of("src/test/resources/data-backup.json"),
                Path.of("src/main/resources/data.json"),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordServiceTest.class);
    private final MedicalRecordService medicalRecordService = new MedicalRecordService(new JsonDataLoader());

    @Test
    void testGetAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();
        assertNotNull(medicalRecords);
        assertFalse(medicalRecords.isEmpty());
    }

    @Test
    void testAddMedicalRecord() {
        MedicalRecord record = new MedicalRecord("Doudou", "LeZebre", "01/01/1990", List.of("Aspirin"), List.of("Peanuts"));
        assertTrue(medicalRecordService.addMedicalRecord(record));
    }

    @Test
    void testUpdateMedicalRecord() {
        MedicalRecord updatedRecord = new MedicalRecord("John", "Boyd", "03/06/1984",
                List.of("doliprane:500mg", "med2:250mg"),
                List.of("pollen", "nuts"));

        assertTrue(medicalRecordService.updateMedicalRecord("John", "Boyd", updatedRecord));
    }


    @Test
    void testDeleteMedicalRecord() {
        assertTrue(medicalRecordService.deleteMedicalRecord("John", "Boyd"));
    }

    @Test
    void testGetMedicalRecordByName_found() {
        Optional<MedicalRecord> result = medicalRecordService.getMedicalRecordByName("John", "Boyd");
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testGetMedicalRecordByName_notFound() {
        Optional<MedicalRecord> result = medicalRecordService.getMedicalRecordByName("Fake", "User");
        assertFalse(result.isPresent());
    }
    @Test
    void testAddMedicalRecord_duplicate() {
        MedicalRecord existingRecord = new MedicalRecord("John", "Boyd", "03/06/1984",
                List.of("med1:500mg"),
                List.of("pollen"));
        boolean result = medicalRecordService.addMedicalRecord(existingRecord);
        assertFalse(result);
    }
    @Test
    void testUpdateMedicalRecord_notFound() {
        MedicalRecord updated = new MedicalRecord("Ghost", "User", "01/01/2000", List.of(), List.of());
        boolean result = medicalRecordService.updateMedicalRecord("Ghost", "User", updated);
        assertFalse(result);
    }



}



