package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Ce DTO servira pour les personnes avec des paramètres
 */
@Getter
@Setter
public class PersonDTO {
    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private List<String> medications;
    private List<String> allergies;

    public PersonDTO(Person person, MedicalRecord medicalRecord) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phone = person.getPhone();

        if (medicalRecord != null) {
            this.medications = medicalRecord.getMedications();
            this.allergies = medicalRecord.getAllergies();
            this.age = calculateAge(medicalRecord.getBirthdate());
        } else {
            this.medications = List.of();
            this.allergies = List.of();
            this.age = -1; // Valeur par défaut en cas de problème
        }
    }

    private int calculateAge(String birthdate) {
        if (birthdate == null || birthdate.isEmpty()) {
            return -1;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
