package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Utilisé pour /firestation?stationNumber=<station_number>
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FirestationCoverageDTO {
    private List<PersonDTO> persons;
    private int adultCount;
    private int childCount;


}
