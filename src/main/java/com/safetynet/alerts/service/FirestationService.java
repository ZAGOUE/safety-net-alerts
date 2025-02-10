package com.safetynet.alerts.service;

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

    public Optional<Firestation> getFirestationByAddress(String address) {
        return jsonDataLoader.getAllFirestations().stream()
                .filter(f -> f.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }

    public boolean addFirestation(Firestation firestation) {
        logger.info("🚒 Ajout d'une caserne pour l'adresse : {}", firestation.getAddress());
        if (getFirestationByAddress(firestation.getAddress()).isPresent()) {
            return false;
        }
        jsonDataLoader.getAllFirestations().add(firestation);
        return true;
    }

    public boolean updateFirestation(String address, Firestation updatedFirestation) {
        Optional<Firestation> firestationOpt = getFirestationByAddress(address);
        if (firestationOpt.isPresent()) {
            logger.info("Mise à jouir de la caserne pour l'adresse : {}", address);
            firestationOpt.get().setStation(updatedFirestation.getStation());
            return true;
        }
        logger.error("caserne non trouvé à l'adresse : {}", address);
        return false;
    }

    public boolean deleteFirestation(String address) {
        boolean removed = jsonDataLoader.getAllFirestations().removeIf(f -> f.getAddress().equalsIgnoreCase(address));

        if (removed) {
            logger.info("✅ Caserne supprimée à l'adresse: {}", address);
        } else {
            logger.error("❌ Caserne NON trouvée à l'adresse: {}", address);
        }
        return removed;
    }

    // ✅ **Nouvelle méthode pour `/firestation?stationNumber=<station_number>`**
    public Map<String, Object> getPersonsByStation(int stationNumber) {
        List<Person> personsCovered = jsonDataLoader.getAllPersons().stream()
                .filter(person -> jsonDataLoader.getAllFirestations().stream()
                        .anyMatch(firestation -> firestation.getStation() == stationNumber
                                && firestation.getAddress().equalsIgnoreCase(person.getAddress())))
                .collect(Collectors.toList());

        long adults = personsCovered.stream().filter(p -> p.getAge() > 18).count();
        long children = personsCovered.size() - adults;

        logger.info("{} adultes et {} enfants trouvés pour la station {}", adults, children, stationNumber);

        Map<String, Object> response = new HashMap<>();
        response.put("persons", personsCovered);
        response.put("adults", adults);
        response.put("children", children);

        return response;
    }

    // ✅ **Nouvelle méthode pour `/phoneAlert?firestation=<firestation_number>`**
    public List<String> getPhoneNumbersByStation(int stationNumber) {
        return jsonDataLoader.getAllPersons().stream()
                .filter(person -> jsonDataLoader.getAllFirestations().stream()
                        .anyMatch(firestation -> firestation.getStation() == stationNumber
                                && firestation.getAddress().equalsIgnoreCase(person.getAddress())))
                .map(Person::getPhone)
                .distinct()
                .collect(Collectors.toList());
    }

    public Map<String, Object> getFireAlertByAddress(String address) {
        Optional<Firestation> firestation = jsonDataLoader.getAllFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst();

        List<Person> residents = jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        if (firestation.isEmpty() || residents.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Map<String, Object>> residentDetails = residents.stream()
                .map(person -> Map.of(
                        "firstName", person.getFirstName(),
                        "lastName", person.getLastName(),
                        "phone", person.getPhone(),
                        "age", person.getAge(),
                        "medications", jsonDataLoader.getAllMedicalRecords().stream()
                                .filter(mr -> mr.getFirstName().equals(person.getFirstName()) &&
                                        mr.getLastName().equals(person.getLastName()))
                                .map(MedicalRecord::getMedications)
                                .findFirst()
                                .orElse(Collections.emptyList()),
                        "allergies", jsonDataLoader.getAllMedicalRecords().stream()
                                .filter(mr -> mr.getFirstName().equals(person.getFirstName()) &&
                                        mr.getLastName().equals(person.getLastName()))
                                .map(MedicalRecord::getAllergies)
                                .findFirst()
                                .orElse(Collections.emptyList())
                ))
                .collect(Collectors.toList());

        return Map.of(
                "firestationNumber", firestation.get().getStation(),
                "residents", residentDetails
        );
    }

    public Map<String, List<Map<String, Object>>> getFloodStations(List<Integer> stationNumbers) {
        // Récupérer toutes les adresses associées aux numéros de casernes demandées
        List<String> addresses = jsonDataLoader.getAllFirestations().stream()
                .filter(fs -> stationNumbers.contains(fs.getStation()))
                .map(Firestation::getAddress)
                .distinct()
                .collect(Collectors.toList());

        // Regrouper les habitants par adresse
        Map<String, List<Map<String, Object>>> households = new HashMap<>();
        for (String address : addresses) {
            List<Map<String, Object>> residents = jsonDataLoader.getAllPersons().stream()
                    .filter(person -> person.getAddress().equalsIgnoreCase(address))
                    .map(person -> Map.of(
                            "firstName", person.getFirstName(),
                            "lastName", person.getLastName(),
                            "phone", person.getPhone(),
                            "age", person.getAge(),
                            "medications", jsonDataLoader.getAllMedicalRecords().stream()
                                    .filter(mr -> mr.getFirstName().equals(person.getFirstName()) &&
                                            mr.getLastName().equals(person.getLastName()))
                                    .map(MedicalRecord::getMedications)
                                    .findFirst()
                                    .orElse(Collections.emptyList()),
                            "allergies", jsonDataLoader.getAllMedicalRecords().stream()
                                    .filter(mr -> mr.getFirstName().equals(person.getFirstName()) &&
                                            mr.getLastName().equals(person.getLastName()))
                                    .map(MedicalRecord::getAllergies)
                                    .findFirst()
                                    .orElse(Collections.emptyList())
                    ))
                    .collect(Collectors.toList());

            if (!residents.isEmpty()) {
                households.put(address, residents);
            }
        }

        return households;
    }


}
