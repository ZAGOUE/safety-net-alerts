package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireDTO;
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
    /**
     * Ajoute une nouvelle caserne.
     */
    public boolean addFirestation(Firestation firestation) {
        List<Firestation> stations = jsonDataLoader.getAllFirestations();
        // Vérifie si l'adresse existe déjà
        if (stations.stream().anyMatch(fs -> fs.getAddress().equalsIgnoreCase(firestation.getAddress()))) {
            return false;
        }
        stations.add(firestation);
        return true;
    }
    /**
     * Supprime une caserne par son adresse.
     */
    public boolean deleteFirestation(String address) {
        List<Firestation> stations = jsonDataLoader.getAllFirestations();
        return stations.removeIf(fs -> fs.getAddress().equalsIgnoreCase(address));
    }

    // Récupérer les personnes couvertes par une caserne
    public Map<String, Object> getPersonsByStation(int stationNumber) {
        List<Person> persons = jsonDataLoader.getAllPersons().stream()
                .filter(p -> jsonDataLoader.getAllFirestations().stream()
                        .anyMatch(fs -> fs.getStation() == stationNumber && fs.getAddress().equalsIgnoreCase(p.getAddress())))
                .toList();

        List<MedicalRecord> medicalRecords = jsonDataLoader.getAllMedicalRecords();

        List<Map<String, String>> personDetails = persons.stream()
                .map(person -> Map.of(
                        "firstName", person.getFirstName(),
                        "lastName", person.getLastName(),
                        "address", person.getAddress(),
                        "phone", person.getPhone()
                )).toList();

        long adults = persons.stream()
                .map(person -> medicalRecords.stream()
                        .filter(med -> med.getFirstName().equals(person.getFirstName()) &&
                                med.getLastName().equals(person.getLastName()))
                        .findFirst()
                        .map(MedicalRecord::getAge)  // Récupère l'âge
                        .orElse(0)) // Si non trouvé, âge = 0
                .filter(age -> age > 18)
                .count();

        long children = persons.size() - adults;

        Map<String, Object> result = new HashMap<>();
        result.put("persons", personDetails);
        result.put("adultCount", adults);
        result.put("childCount", children);

        return result;
    }


    /**
     * Récupérer les numéros de téléphone des résidents couverts par une caserne
     */
    public List<String> getPhoneNumbersByStation(int stationNumber) {
        return jsonDataLoader.getAllPersons().stream()
                .filter(person -> jsonDataLoader.getAllFirestations().stream()
                        .anyMatch(firestation -> firestation.getStation() == stationNumber
                                && firestation.getAddress().equalsIgnoreCase(person.getAddress())))
                .map(Person::getPhone)
                .distinct()
                .collect(Collectors.toList());
    }

   /**
    Récupérer les habitants d'une adresse et leur caserne associée
    **/

   // Récupérer les adresses d'une caserne
    public Firestation getFirestationByAddress(String address) {
        return jsonDataLoader.getAllFirestations().stream()
                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(address))
                .findFirst().orElse(null);
    }
    public Map<String, List<PersonDTO>> getFloodStations(List<Integer> stations) {
        Map<String, List<PersonDTO>> result = new HashMap<>();

        for (Integer station : stations) {
            List<String> addresses = jsonDataLoader.getAllFirestations().stream()
                    .filter(fs -> fs.getStation() == station)
                    .map(Firestation::getAddress)
                    .toList();

            for (String address : addresses) {
                List<PersonDTO> residents = jsonDataLoader.getAllPersons().stream()
                        .filter(p -> p.getAddress().equalsIgnoreCase(address))
                        .map(person -> {
                            MedicalRecord record = jsonDataLoader.getAllMedicalRecords().stream()
                                    .filter(med -> med.getFirstName().equals(person.getFirstName()) && med.getLastName().equals(person.getLastName()))
                                    .findFirst()
                                    .orElse(null);
                            return new PersonDTO(person, record);
                        })
                        .toList();
                result.put(address, residents);
            }
        }
        return result;
    }
    /**
     * Trouve une caserne par son adresse.
     */
    public Firestation findByAddress(String address) {
        return jsonDataLoader.getAllFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst()
                .orElse(null);
    }


    /**
     * Recherche les informations des habitants et leurs antécédents médicaux
     */
    public FireDTO getFireInfoByAddress(String address) {
        // Filtrer les personnes par adresse
        List<Person> persons = jsonDataLoader.getAllPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();

        if (persons.isEmpty()) {
            throw new IllegalArgumentException("Aucun habitant trouvé à cette adresse.");
        }

        // Mapper chaque personne en PersonDTO
        List<PersonDTO> residents = persons.stream()
                .map(person -> {
                    MedicalRecord record = jsonDataLoader.getAllMedicalRecords().stream()
                            .filter(med -> med.getFirstName().equals(person.getFirstName()) && med.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);
                    return new PersonDTO(person, record);
                })
                .toList();

        // Récupérer le numéro de la station
        int stationNumber = jsonDataLoader.getAllFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .map(Firestation::getStation)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aucune caserne trouvée pour cette adresse."));

        // Retourner un FireDTO
        return new FireDTO(residents, stationNumber);
    }


    /**
     * Recherche les informations des enfants et leurs antécédents médicaux
     */

    public Map<String, Object> getChildrenByAddress(String address) {
        List<Person> residents = jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        if (residents.isEmpty()) {
            return Map.of("message", "Aucun résident trouvé à cette adresse.");
        }

        List<Map<String, Object>> children = new ArrayList<>();
        List<Map<String, String>> householdMembers = new ArrayList<>();

        for (Person person : residents) {
            MedicalRecord record = jsonDataLoader.getAllMedicalRecords().stream()
                    .filter(med -> med.getFirstName().equals(person.getFirstName()) && med.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElse(null);

            int age = record != null ? record.getAge() : 0;

            if (age <= 18) {
                children.add(Map.of(
                        "firstName", person.getFirstName(),
                        "lastName", person.getLastName(),
                        "age", age
                ));
            } else {
                householdMembers.add(Map.of(
                        "firstName", person.getFirstName(),
                        "lastName", person.getLastName()
                ));
            }
        }

        return Map.of(
                "children", children,
                "householdMembers", householdMembers
        );
    }




}
