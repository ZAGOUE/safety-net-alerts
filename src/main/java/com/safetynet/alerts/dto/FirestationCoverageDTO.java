package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Utilisé pour /firestation?stationNumber=<station_number>
@Getter
@Setter
@AllArgsConstructor
public class FirestationCoverageDTO {
    private List<PersonDTO> persons; // Liste des personnes couvertes
    private long adults; // Nombre d'adultes
    private long children; // Nombre d'enfants
}
