package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// Utilisé pour /childAlert?address=<address>
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildAlertDTO {
    private String firstName;
    private String lastName;
    private int age;
    private List<PersonDTO> householdMembers;
}