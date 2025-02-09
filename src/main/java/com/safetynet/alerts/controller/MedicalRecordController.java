package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.addMedicalRecord(medicalRecord) ?
                ResponseEntity.ok("Medical record added successfully") :
                ResponseEntity.badRequest().body("Failed to add medical record");
    }

    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> updateMedicalRecord(@PathVariable String firstName, @PathVariable String lastName, @RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecord) ?
                ResponseEntity.ok("Medical record updated successfully") :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
        return medicalRecordService.deleteMedicalRecord(firstName, lastName) ?
                ResponseEntity.ok("Medical record deleted successfully") :
                ResponseEntity.notFound().build();
    }
}

