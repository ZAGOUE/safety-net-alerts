package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        logger.info("Requête GET - Récupération de tous les dossiers médicaux.");
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
    }

    @GetMapping("/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecord> getMedicalRecordByName(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("Requête GET - Recherche du dossier médical de {} {}", firstName, lastName);
        Optional<MedicalRecord> record = medicalRecordService.getMedicalRecordByName(firstName, lastName);
        return record.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("Requête POST - Ajout d'un dossier médical pour {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (medicalRecordService.addMedicalRecord(medicalRecord)) {
            return ResponseEntity.ok("Dossier médical ajouté avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Un dossier médical existe déjà.");
        }
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("Requête DELETE - Suppression du dossier médical de {} {}", firstName, lastName);
        if (medicalRecordService.deleteMedicalRecord(firstName, lastName)) {
            return ResponseEntity.ok("Suppression réussie.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
