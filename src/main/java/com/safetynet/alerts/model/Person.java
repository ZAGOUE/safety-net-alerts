package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    @JsonProperty("birthdate")
    private String birthdate;

    public Person(String firstName, String lastName, String address, String city, String zip, String phone, String email, String birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.birthdate = birthdate;
    }

    /**
     * Conversion de `birthdate` en LocalDate
     */
    @JsonIgnore
    public LocalDate getBirthdateAsLocalDate() {
        if (this.birthdate == null || this.birthdate.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(this.birthdate, formatter);
    }


    /**
     * Méthode pour obtenir l'âge
     * @return -1 si la date est invalide
     */
    @JsonIgnore
    public int getAge() {
        LocalDate birthDate = getBirthdateAsLocalDate();
        if (birthDate == null) {
            return -1;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}
