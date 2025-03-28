package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

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
            this.age = -1;
        }
    }

    private int calculateAge(String birthdate) {
        if (birthdate == null || birthdate.isEmpty()) {
            return -1;
        }

        LocalDate birthDate;
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        try {
            birthDate = LocalDate.parse(birthdate, formatter1); // Essai avec MM/dd/yyyy
        } catch (DateTimeParseException e1) {
            try {
                birthDate = LocalDate.parse(birthdate, formatter2); // Essai avec yyyy-MM-dd
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Format de date non pris en charge : " + birthdate);
            }
        }

        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}

