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
        if (this.birthdate == null || this.birthdate.isEmpty()) {
            System.out.println("⚠️ Birthdate NULL ou vide pour " + this.firstName + " " + this.lastName);
            return null;
        }
        System.out.println("📅 Birthdate chargé pour " + this.firstName + " " + this.lastName + " : " + this.birthdate);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return LocalDate.parse(this.birthdate, formatter);
        } catch (Exception e) {
            System.out.println("❌ ERREUR : Impossible de convertir la date " + this.birthdate);
            return null;
        }
    }

    // ✅ **Méthode pour calculer l'âge**
    public int getAge() {
        LocalDate birthDate = getBirthdateAsLocalDate();
        if (birthDate == null) {
            return -1; // Retourne -1 si la date est invalide
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
