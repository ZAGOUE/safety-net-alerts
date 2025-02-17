package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Utilisé pour /childAlert?address=<address>
@Getter
@Setter
@AllArgsConstructor
public class ChildAlertDTO {
    private List<ChildDTO> children; // Liste des enfants
    private List<PersonDTO> householdMembers; // Autres membres du foyer
}
