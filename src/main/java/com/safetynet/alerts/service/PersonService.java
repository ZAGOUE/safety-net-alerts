package com.safetynet.alerts.service;


import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
    private final JsonDataLoader jsonDataLoader;

    public PersonService(JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    // ✅ Récupérer les enfants et les adultes d'un foyer
    public Map<String, Object> getChildrenByAddress(String address) {
        List<Person> personsAtAddress = jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        List<MedicalRecord> medicalRecords = jsonDataLoader.getAllMedicalRecords();

        List<PersonDTO> children = personsAtAddress.stream()
                .filter(person -> person.getAge() <= 18)
                .map(person -> {
                    MedicalRecord medicalRecord = medicalRecords.stream()
                            .filter(med -> med.getFirstName().equals(person.getFirstName()) && med.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);
                    return new PersonDTO(person, medicalRecord);
                })
                .collect(Collectors.toList());

        List<PersonDTO> adults = personsAtAddress.stream()
                .filter(person -> person.getAge() > 18)
                .map(person -> {
                    MedicalRecord medicalRecord = medicalRecords.stream()
                            .filter(med -> med.getFirstName().equals(person.getFirstName()) && med.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);
                    return new PersonDTO(person, medicalRecord);
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("children", children);
        response.put("householdMembers", adults);

        return response;
    }

    // ✅ Récupérer les emails d'une ville
    public Set<String> getCommunityEmails(String city) {
        return jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .collect(Collectors.toSet());
    }


    public Optional<Person> getPersonByName(String firstName, String lastName) {
        return jsonDataLoader.getAllPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst(); // ✅ Retourne un Optional<Person>
    }


    public boolean addPerson(Person person) {
        List<Person> persons = jsonDataLoader.getAllPersons();
        if (persons.stream().noneMatch(p -> p.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                p.getLastName().equalsIgnoreCase(person.getLastName()))) {
            persons.add(person);
            return true;
        }
        return false;
    }

    public boolean updatePerson(String firstName, String lastName, Person updatedPerson) {
        Optional<Person> personOpt = getPersonByName(firstName, lastName);
        if (personOpt.isPresent()) { // ✅ Vérifie si la personne existe
            Person person = personOpt.get(); // ✅ Récupère l'objet Person de l'Optional
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
        return jsonDataLoader.getAllPersons().removeIf(
                p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName));
    }
    private String convertListToString(List<String> list) {
        return (list != null) ? String.join(", ", list) : "";
    }



    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) {
        return jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .map(person -> {
                    MedicalRecord record = jsonDataLoader.getAllMedicalRecords().stream()
                            .filter(med -> med.getFirstName().equalsIgnoreCase(person.getFirstName())
                                    && med.getLastName().equalsIgnoreCase(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    // 🧮 Calcul de l'âge
                    int age = record != null ? calculateAge(record.getBirthdate()) : 0;

                    // 🔄 Conversion des listes en chaînes de caractères
                    String medications = record != null ? String.join(", ", record.getMedications()) : "";
                    String allergies = record != null ? String.join(", ", record.getAllergies()) : "";

                    return new PersonInfoDTO(
                            person.getLastName(),
                            person.getAddress(),
                            person.getEmail(),
                            age,
                            medications,
                            allergies
                    );
                })
                .collect(Collectors.toList());
    }




    private int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }


    /**
     * Récupérer toutes les personnes.
     *
     * @return Liste de PersonDTO.
     */
    public List<PersonDTO> getAllPersons() {
        logger.info("🛠️ Récupération de toutes les personnes...");

        List<Person> persons = jsonDataLoader.getAllPersons();
        if (persons == null || persons.isEmpty()) {
            logger.warn("⚠️ Aucune personne trouvée dans les données !");
            return List.of();
        }

        // Conversion de la liste de Person en PersonDTO
        List<PersonDTO> personDTOList = persons.stream()
                .map(person -> new PersonDTO(person, null))
                .collect(Collectors.toList());

        logger.info("✅ {} personnes trouvées et converties en PersonDTO", personDTOList.size());
        return personDTOList;
    }
}

