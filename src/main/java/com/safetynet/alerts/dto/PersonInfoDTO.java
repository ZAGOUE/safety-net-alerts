package com.safetynet.alerts.dto;


import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class PersonInfoDTO {

    // 🔹 Getters & Setters
    private String lastName;
    private String address;
    private  String email;
    private int age;
    private String medications;
    private String allergies;

    // 🔹 Constructeur attendu


    public PersonInfoDTO(String lastName, String address, String email, int age, String medications, String allergies) {
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }


    @Override
    public String toString() {
        return "PersonInfoDTO{" +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", medications='" + medications + '\'' +
                ", allergies='" + allergies + '\'' +
                '}';
    }
}
