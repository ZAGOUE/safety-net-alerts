package com.safetynet.alerts.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void testPersonModel() {
        Person person = new Person("John", "Doe", "123 St", "City", "12345", "000-0000", "john@example.com", "01/01/1990");
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("123 St", person.getAddress());
        assertEquals("City", person.getCity());
        assertEquals("12345", person.getZip());
        assertEquals("000-0000", person.getPhone());
        assertEquals("john@example.com", person.getEmail());
        assertEquals("01/01/1990", person.getBirthdate());

        // Setters (si Lombok ou pas de constructeur par défaut)
        Person p = new Person();
        p.setFirstName("Alice");
        p.setLastName("Smith");
        p.setAddress("456 Avenue");
        p.setCity("Paris");
        p.setZip("75000");
        p.setPhone("111-222-333");
        p.setEmail("alice@example.com");
        p.setBirthdate("02/02/1980");

        assertEquals("Alice", p.getFirstName());
        assertEquals("Paris", p.getCity());
    }

    @Test
    public void testFirestationModel() {
        Firestation fs = new Firestation("1509 Culver St", 3);
        assertEquals("1509 Culver St", fs.getAddress());
        assertEquals(3, fs.getStation());

        fs.setStation(1);
        fs.setAddress("123 St");
        assertEquals("123 St", fs.getAddress());
        assertEquals(1, fs.getStation());
    }

    @Test
    public void testMedicalRecordModel() {
        MedicalRecord mr = new MedicalRecord("Jane", "Doe", "12/31/1985",
                java.util.Arrays.asList("med1", "med2"),
                java.util.Arrays.asList("allergy1"));

        assertEquals("Jane", mr.getFirstName());
        assertEquals("Doe", mr.getLastName());
        assertEquals("12/31/1985", mr.getBirthdate());
        assertEquals(2, mr.getMedications().size());
        assertEquals(1, mr.getAllergies().size());

        mr.setBirthdate("01/01/1990");
        assertEquals("01/01/1990", mr.getBirthdate());
    }

    @Test
    void testGetAge() {
        Person person = new Person("John", "Doe", "123 Main St", "City", "12345", "123-4567", "john@example.com", "01/01/2000");
        int age = person.getAge();
        assertTrue(age > 0); // ou assertEquals(expectedAge, age) si tu veux tester une valeur précise
    }

    @Test
    void testGetBirthdateAsLocalDate() {
        Person person = new Person("John", "Doe", "123 Main St", "City", "12345", "123-4567", "john@example.com", "01/01/2000");
        LocalDate birthDate = person.getBirthdateAsLocalDate();
        assertEquals(LocalDate.of(2000, 1, 1), birthDate);
    }



}
