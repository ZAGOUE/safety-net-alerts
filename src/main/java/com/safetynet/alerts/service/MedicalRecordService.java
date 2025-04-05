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

    private final JsonDataLoader jsonDataLoader;




    public MedicalRecordService(JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
        this.medicalRecords = jsonDataLoader.getAllMedicalRecords();
        logger.info("Chargement des dossiers médicaux terminé.");
    }

    /**
     * Récupération de tous les dossiers médicaux.
     *
     */

    public List<MedicalRecord> getAllMedicalRecords() {
        logger.debug("Récupération de tous les dossiers médicaux.");
        return medicalRecords;
    }

    /**
     * Recherche du dossier médical por le nom de famille
     *
     */

    public Optional<MedicalRecord> getMedicalRecordByName(String firstName, String lastName) {
        logger.debug("Recherche du dossier médical pour : {} {}", firstName, lastName);
        return medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName)
                        && medicalRecord.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public boolean addMedicalRecord(MedicalRecord medicalRecord) {
        List<MedicalRecord> records = jsonDataLoader.getAllMedicalRecords();

        boolean exists = records.stream()
                .anyMatch(r -> r.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
                        && r.getLastName().equalsIgnoreCase(medicalRecord.getLastName()));

        if (exists) {
            logger.warn("Ajout échoué : dossier médical existe déjà pour {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
            return false;
        }

        records.add(medicalRecord);
        jsonDataLoader.saveAllMedicalRecords(records);
        logger.info("Dossier médical ajouté avec succès pour {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        return true;
    }




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
            jsonDataLoader.saveAllMedicalRecords(medicalRecords);
            return true;
        }
        logger.warn("Dossier médical à mettre à jour introuvable : {} {}", firstName, lastName);

        return false;
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        if (medicalRecords.removeIf(record -> record.getFirstName().equalsIgnoreCase(firstName)
                && record.getLastName().equalsIgnoreCase(lastName))) {
            jsonDataLoader.saveAllMedicalRecords(medicalRecords);
            logger.info("Suppression réussie du dossier médical de {} {}", firstName, lastName);
            return true;
        } else {
            logger.error("Échec : Aucun dossier médical trouvé pour {} {}", firstName, lastName);
            return false;
        }
    }
}
