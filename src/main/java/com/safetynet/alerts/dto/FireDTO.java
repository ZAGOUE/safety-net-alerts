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
    private List<PersonDTO> residents;
    private int stationNumber;

    @Override
    public String toString() {
        return "FireDTO{" +
                "residents=" + residents +
                ", stationNumber=" + stationNumber +
                '}';
    }
}
