package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonBasicDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirestationService {
    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);
    private final JsonDataLoader jsonDataLoader;

    public FirestationService(JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    public List<Firestation> getAllFirestations() {
        return jsonDataLoader.getAllFirestations();
    }
   // Mettre à jour les informations de la caserne
    public  boolean updateFirestation(String address, Firestation updateFirestation) {
        List<Firestation> firestations = jsonDataLoader.getAllFirestations();
        for (Firestation firestation : firestations) {
            if (firestation.getAddress().equalsIgnoreCase(address)) {
                firestation.setStation(updateFirestation.getStation());
                logger.info("firestation is up to date : {}", address);
                return true;
            }
        }
        logger.warn("firestation not found for update : {}", address);
        return false;
    }

    // ✅ Récupérer les personnes couvertes par une caserne
    public Map<String, Object> getPersonsByStation(int stationNumber) {
        List<Person> personsCovered = jsonDataLoader.getAllPersons().stream()
                .filter(person -> jsonDataLoader.getAllFirestations().stream()
                        .anyMatch(firestation -> firestation.getStation() == stationNumber
                                && firestation.getAddress().equalsIgnoreCase(person.getAddress())))
                .collect(Collectors.toList());

        long adults = personsCovered.stream().filter(p -> p.getAge() > 18).count();
        long children = personsCovered.size() - adults;

        Map<String, Object> response = new HashMap<>();
        response.put("persons", personsCovered.stream()
                .map(person -> new PersonBasicDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()))
                .collect(Collectors.toList()));
        response.put("adults", adults);
        response.put("children", children);

        return response;
    }

    // ✅ Récupérer les numéros de téléphone des résidents couverts par une caserne
    public List<String> getPhoneNumbersByStation(int stationNumber) {
        return jsonDataLoader.getAllPersons().stream()
                .filter(person -> jsonDataLoader.getAllFirestations().stream()
                        .anyMatch(firestation -> firestation.getStation() == stationNumber
                                && firestation.getAddress().equalsIgnoreCase(person.getAddress())))
                .map(Person::getPhone)
                .distinct()
                .collect(Collectors.toList());
    }

    // ✅ Récupérer les habitants d'une adresse et leur caserne associée
    public Map<String, Object> getFireDetails(String address) {
        List<Person> residents = jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        Optional<Firestation> firestation = jsonDataLoader.getAllFirestations().stream()
                .filter(f -> f.getAddress().equalsIgnoreCase(address))
                .findFirst();

        List<PersonDTO> residentDetails = residents.stream()
                .map(person -> {
                    MedicalRecord medicalRecord = jsonDataLoader.getAllMedicalRecords().stream()
                            .filter(med -> med.getFirstName().equals(person.getFirstName()) && med.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);
                    return new PersonDTO(person, medicalRecord);
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("stationNumber", firestation.map(Firestation::getStation).orElse(null));
        response.put("residents", residentDetails);

        return response;
    }

   // Récupérer les adresses d'une caserne
    public Firestation getFirestationByAddress(String address) {
        return jsonDataLoader.getAllFirestations().stream()
                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(address))
                .findFirst().orElse(null);
    }
    public Map<String, List<PersonDTO>> getFloodStations(List<Integer> stationNumbers) {
        // Récupération des adresses desservies par les stations fournies
        Set<String> addresses = jsonDataLoader.getAllFirestations().stream()
                .filter(firestation -> stationNumbers.contains(firestation.getStation()))
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());

        // Récupération des habitants pour chaque adresse
        Map<String, List<PersonDTO>> result = new HashMap<>();
        for (String address : addresses) {
            List<Person> residents = jsonDataLoader.getAllPersons().stream()
                    .filter(person -> person.getAddress().equalsIgnoreCase(address))
                    .collect(Collectors.toList());

            List<PersonDTO> personDTOs = residents.stream()
                    .map(person -> {
                        MedicalRecord medicalRecord = jsonDataLoader.getAllMedicalRecords().stream()
                                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
                                        && record.getLastName().equalsIgnoreCase(person.getLastName()))
                                .findFirst()
                                .orElse(null);

                        return new PersonDTO(person, medicalRecord);
                    })
                    .collect(Collectors.toList());

            result.put(address, personDTOs);
        }
        return result;
    }

    // Recherche les informations des habitants et leurs antécédents médicaux
    public List<PersonDTO> getFireInfoByAddress(String address) {
        List<Person> residents = jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
        return residents.stream().map(person -> {
                    MedicalRecord medicalRecord = jsonDataLoader.getAllMedicalRecords().stream()
                            .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
                                    && record.getLastName().equalsIgnoreCase(person.getLastName()))
                            .findFirst().orElse(null);
                    return new PersonDTO(person, medicalRecord);
                })
                .collect(Collectors.toList());
    }



}
