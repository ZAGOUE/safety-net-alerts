package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
    private final List<Person> persons;

    public PersonService(JsonDataLoader jsonDataLoader) {
        this.persons = jsonDataLoader.getData().getPersons();
        logger.info("✅ Chargement des données des personnes terminé.");
    }

    public List<Person> getAllPersons() {
        logger.debug("🔍 Récupération de toutes les personnes.");
        return persons;
    }

    public Optional<Person> getPersonByName(String firstName, String lastName) {
        logger.debug("🔍 Recherche de la personne : {} {}", firstName, lastName);
        return persons.stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName)
                        && person.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public boolean addPerson(Person person) {
        if (getPersonByName(person.getFirstName(), person.getLastName()).isPresent()) {
            logger.error("❌ Échec : La personne {} {} existe déjà.", person.getFirstName(), person.getLastName());
            return false;
        }
        persons.add(person);
        logger.info("✅ Nouvelle personne ajoutée : {} {}", person.getFirstName(), person.getLastName());
        return true;
    }

    public boolean updatePerson(String firstName, String lastName, Person updatedPerson) {
        Optional<Person> personOpt = getPersonByName(firstName, lastName);
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            logger.debug("🔄 Mise à jour de la personne {} {}", firstName, lastName);
            person.setAddress(updatedPerson.getAddress());
            person.setCity(updatedPerson.getCity());
            person.setZip(updatedPerson.getZip());
            person.setPhone(updatedPerson.getPhone());
            person.setEmail(updatedPerson.getEmail());
            logger.info("✅ Mise à jour réussie pour {} {}", firstName, lastName);
            return true;
        } else {
            logger.error("❌ Échec : La personne {} {} n'existe pas.", firstName, lastName);
            return false;
        }
    }

    public boolean deletePerson(String firstName, String lastName) {
        if (persons.removeIf(person -> person.getFirstName().equalsIgnoreCase(firstName)
                && person.getLastName().equalsIgnoreCase(lastName))) {
            logger.info("🗑️ Suppression réussie de la personne {} {}", firstName, lastName);
            return true;
        } else {
            logger.error("❌ Échec : Impossible de supprimer {} {}, personne non trouvée.", firstName, lastName);
            return false;
        }
    }
}
