package com.safetynet.alerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FloodDTO {
    private String address;
    private List<PersonDTO> persons;

    public FloodDTO(String address, List<PersonDTO> persons) {
        this.address = address;
        this.persons = persons;
    }
}
