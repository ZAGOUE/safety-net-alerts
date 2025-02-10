package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);
    private final List<MedicalRecord> medicalRecords;

    public MedicalRecordService(JsonDataLoader jsonDataLoader) {
        this.medicalRecords = jsonDataLoader.getData().getMedicalrecords();
        logger.info("✅ Chargement des dossiers médicaux terminé.");
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        logger.debug("🔍 Récupération de tous les dossiers médicaux.");
        return medicalRecords;
    }

    public Optional<MedicalRecord> getMedicalRecordByName(String firstName, String lastName) {
        logger.debug("🔍 Recherche du dossier médical pour : {} {}", firstName, lastName);
        return medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName)
                        && medicalRecord.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public boolean addMedicalRecord(MedicalRecord medicalRecord) {
        if (getMedicalRecordByName(medicalRecord.getFirstName(), medicalRecord.getLastName()).isPresent()) {
            logger.error("❌ Échec : Un dossier médical existe déjà pour {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
            return false;
        }
        medicalRecords.add(medicalRecord);
        logger.info("✅ Nouveau dossier médical ajouté pour {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        return true;
    }

    // ✅ Implémentation de updateMedicalRecord
    public boolean updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedRecord) {
        Optional<MedicalRecord> existingRecord = medicalRecords.stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(firstName)
                        && record.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (existingRecord.isPresent()) {
            MedicalRecord recordToUpdate = existingRecord.get();
            recordToUpdate.setBirthdate(updatedRecord.getBirthdate());
            recordToUpdate.setMedications(updatedRecord.getMedications());
            recordToUpdate.setAllergies(updatedRecord.getAllergies());
            return true; // ✅ Mise à jour réussie
        }
        return false; // ❌ Dossier médical non trouvé
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        if (medicalRecords.removeIf(record -> record.getFirstName().equalsIgnoreCase(firstName)
                && record.getLastName().equalsIgnoreCase(lastName))) {
            logger.info("🗑️ Suppression réussie du dossier médical de {} {}", firstName, lastName);
            return true;
        } else {
            logger.error("❌ Échec : Aucun dossier médical trouvé pour {} {}", firstName, lastName);
            return false;
        }
    }
}
