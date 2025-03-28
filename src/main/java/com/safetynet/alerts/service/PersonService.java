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


    /**
     * Récupérer les emails d'une ville
     */
    public Set<String> getCommunityEmails(String city) {
        logger.debug("Récupération des emails pour la ville : {}", city);
        return jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .collect(Collectors.toSet());

    }


    public Optional<Person> getPersonByName(String firstName, String lastName) {
        return jsonDataLoader.getAllPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst(); // Retourne un Optional<Person>
    }


    public boolean addPerson(Person person) {
        List<Person> persons = jsonDataLoader.getAllPersons();
        if (persons.stream().noneMatch(p -> p.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                p.getLastName().equalsIgnoreCase(person.getLastName()))) {
            persons.add(person);
            logger.info("Ajout d'une nouvelle personne : {} {}", person.getFirstName(), person.getLastName());

            return true;
        }
        logger.warn("Ajout échoué : la personne {} {} existe déjà", person.getFirstName(), person.getLastName());

        return false;
    }

    public boolean updatePerson(String firstName, String lastName, Person updatedPerson) {
        Optional<Person> personOpt = getPersonByName(firstName, lastName);
        if (personOpt.isPresent()) { // Vérifie si la personne existe
            Person person = personOpt.get();
            person.setAddress(updatedPerson.getAddress());
            person.setCity(updatedPerson.getCity());
            person.setZip(updatedPerson.getZip());
            person.setPhone(updatedPerson.getPhone());
            person.setEmail(updatedPerson.getEmail());
            logger.info("Mise à jour réussie pour : {} {}", firstName, lastName);

            return true;
        }
        logger.warn("Mise à jour échouée : personne non trouvée - {} {}", firstName, lastName);

        return false;
    }

    public boolean deletePerson(String firstName, String lastName) {

       boolean removed = jsonDataLoader.getAllPersons().removeIf(
                p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName));
        if (removed) {
            logger.info("Suppression réussie de la personne : {} {}", firstName, lastName);
        } else {
            logger.warn("Suppression échouée : personne introuvable - {} {}", firstName, lastName);
        }
        return removed;

    }

    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) {
        logger.info("Recherche des informations pour les personnes avec le nom : {}", lastName);

        List<PersonInfoDTO> results = jsonDataLoader.getAllPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .map(person -> {
                    MedicalRecord record = jsonDataLoader.getAllMedicalRecords().stream()
                            .filter(med -> med.getFirstName().equalsIgnoreCase(person.getFirstName())
                                    && med.getLastName().equalsIgnoreCase(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    // Calcul de l'âge
                    int age = record != null ? calculateAge(record.getBirthdate()) : 0;

                    // Conversion des listes en chaînes de caractères
                    String medications = record != null ? String.join(", ", record.getMedications()) : "";
                    String allergies = record != null ? String.join(", ", record.getAllergies()) : "";

                    return new PersonInfoDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getAddress(),
                            person.getEmail(),
                            age,
                            medications,
                            allergies
                    );
                })
                .collect(Collectors.toList());
        logger.info("{} personne(s) trouvée(s) avec le nom de famille '{}'", results.size(), lastName);
        return results;

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
            logger.warn("Aucune personne trouvée dans les données !");
            return List.of();
        }

        // Conversion de la liste de Person en PersonDTO
        List<PersonDTO> personDTOList = persons.stream()
                .map(person -> new PersonDTO(person, null))
                .collect(Collectors.toList());

        logger.info("{} personnes trouvées et converties en PersonDTO", personDTOList.size());
        return personDTOList;
    }
}

