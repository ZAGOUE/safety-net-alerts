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

    /**
     * Mettre à jour les informations de la caserne
     *
     */

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

        if (stations.stream().anyMatch(fs -> fs.getAddress().equalsIgnoreCase(firestation.getAddress()))) {
            return false;
        }
        stations.add(firestation);
        logger.info("Ajout d'une nouvelle caserne : {}", firestation);

        return true;
    }
    /**
     * Supprime une caserne par son adresse.
     */
    public boolean deleteFirestation(String address) {
        List<Firestation> stations = jsonDataLoader.getAllFirestations();
        logger.info("Suppression de la caserne à l'adresse : {}", address);
        return stations.removeIf(fs -> fs.getAddress().equalsIgnoreCase(address));
    }

    /**
     * Récupérer les personnes couvertes par une caserne
     * @return une liste de personne
     */

    //
    public Map<String, Object> getPersonsByStation(int stationNumber) {
        logger.info("Recherche des personnes couvertes par la caserne n°{}", stationNumber);

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
        logger.info("Récupération des numéros de téléphone pour la caserne n°{}", stationNumber);

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
            logger.info("Récupération des foyers pour les casernes : {}", stations);

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
     * @return PersonDTO rt FireDTO
     */
    public FireDTO getFireInfoByAddress(String address) {

        List<Person> persons = jsonDataLoader.getAllPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();

        if (persons.isEmpty()) {
            logger.warn("Aucun habitant trouvé à l'adresse : {}", address);
            throw new IllegalArgumentException("Aucun habitant trouvé à cette adresse.");
        }


        List<PersonDTO> residents = persons.stream()
                .map(person -> {
                    MedicalRecord record = jsonDataLoader.getAllMedicalRecords().stream()
                            .filter(med -> med.getFirstName().equals(person.getFirstName()) && med.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);
                    return new PersonDTO(person, record);
                })
                .toList();


        int stationNumber = jsonDataLoader.getAllFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .map(Firestation::getStation)
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("Aucune caserne trouvée pour l'adresse : {}", address);
                    return new IllegalArgumentException("Aucune caserne trouvée pour cette adresse.");
                });



        logger.info("Informations récupérées pour l'adresse : {}", address);

        return new FireDTO(residents, stationNumber);
    }


    /**
     * Recherche les informations des enfants et leurs antécédents médicaux
     */

    public Map<String, Object> getChildrenByAddress(String address) {
        logger.info("Recherche des enfants à l'adresse : {}", address);

        List<Person> residents = jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        if (residents.isEmpty()) {
            logger.warn("Aucun résident trouvé à l'adresse : {}", address);
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
