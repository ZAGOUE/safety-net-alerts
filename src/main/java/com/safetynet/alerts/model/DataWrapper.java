package com.safetynet.alerts.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DataWrapper {
    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;

}
