package com.safetynet.alerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
