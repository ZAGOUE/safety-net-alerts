package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Utilisé pour /fire?address=<address>
@Getter
@Setter
@AllArgsConstructor
public class FireDTO {
    private String stationNumber; // Numéro de la caserne de pompiers
    private List<PersonMedicalDTO> residents; // Liste des résidents
}
