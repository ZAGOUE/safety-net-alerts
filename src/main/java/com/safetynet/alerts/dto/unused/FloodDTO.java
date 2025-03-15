package com.safetynet.alerts.dto.unused;

import com.safetynet.alerts.dto.PersonDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Utilisé pour flood/stations?stations=1,2,3
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
