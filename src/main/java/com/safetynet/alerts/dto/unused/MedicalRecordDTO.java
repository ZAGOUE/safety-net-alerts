package com.safetynet.alerts.dto.unused;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Ce DTO servira pour les requêtes qui ont besoin des données médicales.
@Getter
@Setter
public class MedicalRecordDTO {
    private List<String> medications;
    private List<String> allergies;

    public MedicalRecordDTO(List<String> medications, List<String> allergies) {
        this.medications = medications;
        this.allergies = allergies;
    }
}
