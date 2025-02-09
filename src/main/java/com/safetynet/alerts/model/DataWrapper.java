package com.safetynet.alerts.model;

import java.util.List;

public class DataWrapper {

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;

    public List<Person> getPersons() {
        return persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
   }

    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }
}
