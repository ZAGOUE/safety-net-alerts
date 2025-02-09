package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {
    private final List<MedicalRecord> medicalRecords;

    public MedicalRecordService(JsonDataLoader jsonDataLoader) {
        this.medicalRecords = jsonDataLoader.getData().getMedicalrecords();
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecords;
    }

    public Optional<MedicalRecord> getMedicalRecord(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName) &&
                        m.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public boolean addMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecords.add(medicalRecord);
    }

    public boolean updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedRecord) {
        return getMedicalRecord(firstName, lastName).map(m -> {
            m.setBirthdate(updatedRecord.getBirthdate());
            m.setMedications(updatedRecord.getMedications());
            m.setAllergies(updatedRecord.getAllergies());
            return true;
        }).orElse(false);
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        return medicalRecords.removeIf(m -> m.getFirstName().equalsIgnoreCase(firstName) &&
                m.getLastName().equalsIgnoreCase(lastName));
    }
}
