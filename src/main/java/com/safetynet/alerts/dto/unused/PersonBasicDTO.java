package com.safetynet.alerts.dto.unused;

import lombok.Getter;
import lombok.Setter;

// Ce DTO servira pour les requêtes qui n'ont n’a pas besoin des données médicales.
@Getter
@Setter
public class PersonBasicDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;


    public PersonBasicDTO(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }
}
