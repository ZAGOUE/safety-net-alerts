package com.safetynet.alerts.dto.unused;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Ajout des antécédents médicaux pour les résidents
@Getter
@Setter
@AllArgsConstructor
public class PersonMedicalDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;
}
