package com.safetynet.alerts.dto.unused;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Représente uniquement un enfant
@Getter
@Setter
@AllArgsConstructor
public class ChildDTO {
    private String firstName;
    private String lastName;
    private int age;
}
