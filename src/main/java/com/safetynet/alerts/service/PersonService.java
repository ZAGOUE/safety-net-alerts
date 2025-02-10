package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
    private final JsonDataLoader jsonDataLoader;

    public PersonService(JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    public List<Person> getAllPersons() {
        return jsonDataLoader.getAllPersons();
    }

    public Optional<Person> getPersonByName(String firstName, String lastName) {
        return jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName)
                        && person.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public boolean addPerson(Person person) {
        logger.info("👤 Ajout d'une personne : {} {}", person.getFirstName(), person.getLastName());
        if (getPersonByName(person.getFirstName(), person.getLastName()).isPresent()) {
            return false;
        }
        jsonDataLoader.getAllPersons().add(person);
        return true;
    }

    public boolean updatePerson(String firstName, String lastName, Person updatedPerson) {
        Optional<Person> personOpt = getPersonByName(firstName, lastName);
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            person.setAddress(updatedPerson.getAddress());
            person.setCity(updatedPerson.getCity());
            person.setZip(updatedPerson.getZip());
            person.setPhone(updatedPerson.getPhone());
            person.setEmail(updatedPerson.getEmail());
            return true;
        }
        return false;
    }

    public boolean deletePerson(String firstName, String lastName) {
        return jsonDataLoader.getAllPersons().removeIf(p ->
                p.getFirstName().equalsIgnoreCase(firstName) &&
                        p.getLastName().equalsIgnoreCase(lastName));
    }

    // ✅ **Nouvelle méthode pour `/childAlert?address=<address>`**
    public Map<String, Object> getChildrenByAddress(String address) {
        List<Person> personsAtAddress = jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        List<MedicalRecord> medicalRecords = jsonDataLoader.getAllMedicalRecords();

        List<Map<String, Object>> children = personsAtAddress.stream()
                .filter(person -> person.getAge() <= 18)  // Filtrer les enfants
                .map(child -> {
                    Map<String, Object> childInfo = new HashMap<>();
                    childInfo.put("firstName", child.getFirstName());
                    childInfo.put("lastName", child.getLastName());
                    childInfo.put("age", child.getAge());  // ✅ Ajout de l'âge
                    return childInfo;
                })
                .collect(Collectors.toList());

        List<Person> adults = personsAtAddress.stream()
                .filter(person -> person.getAge() > 18)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("children", children);
        response.put("householdMembers", adults);

        return response;
    }

    public List<String> getCommunityEmails(String city) {
        return jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPersonInfoByLastName(String lastName) {
        List<MedicalRecord> medicalRecords = jsonDataLoader.getAllMedicalRecords();

        return jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .map(person -> {
                    Map<String, Object> personInfo = new HashMap<>();
                    personInfo.put("firstName", person.getFirstName());
                    personInfo.put("lastName", person.getLastName());
                    personInfo.put("address", person.getAddress());
                    personInfo.put("email", person.getEmail());

                    // Recherche du dossier médical correspondant
                    Optional<MedicalRecord> medicalRecord = medicalRecords.stream()
                            .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                                    record.getLastName().equalsIgnoreCase(person.getLastName()))
                            .findFirst();

                    medicalRecord.ifPresent(record -> {
                        personInfo.put("age", person.getAge());
                        personInfo.put("medications", record.getMedications());
                        personInfo.put("allergies", record.getAllergies());
                    });

                    return personInfo;
                })
                .collect(Collectors.toList());
    }


}
