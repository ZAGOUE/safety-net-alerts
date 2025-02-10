package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // ✅ Nécessaire pour la désérialisation JSON
public class MedicalRecord {
    private String firstName;
    private String lastName;

    @JsonProperty("birthdate") // ✅ Pour bien mapper la clé JSON
    private String birthdate;

    private List<String> medications;
    private List<String> allergies;

    public MedicalRecord(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    // ✅ **Conversion String → LocalDate**
    public LocalDate getBirthdateAsLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(this.birthdate, formatter);
    }

    // ✅ **Méthode pour calculer l'âge**
    public int getAge() {
        return Period.between(getBirthdateAsLocalDate(), LocalDate.now()).getYears();
    }
}
