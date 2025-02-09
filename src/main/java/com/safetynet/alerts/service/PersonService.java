package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final List<Person> persons;

    public PersonService(JsonDataLoader jsonDataLoader) {
        this.persons = jsonDataLoader.getData().getPersons();
    }

    public List<Person> getAllPersons() {
        return persons;
    }

    public Optional<Person> getPerson(String firstName, String lastName) {
        return persons.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) &&
                        p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public boolean addPerson(Person person) {
        return persons.add(person);
    }

    public boolean updatePerson(String firstName, String lastName, Person updatedPerson) {
        return getPerson(firstName, lastName).map(person -> {
            person.setAddress(updatedPerson.getAddress());
            person.setCity(updatedPerson.getCity());
            person.setZip(updatedPerson.getZip());
            person.setPhone(updatedPerson.getPhone());
            person.setEmail(updatedPerson.getEmail());
            return true;
        }).orElse(false);
    }

    public boolean deletePerson(String firstName, String lastName) {
        return persons.removeIf(p -> p.getFirstName().equalsIgnoreCase(firstName) &&
                p.getLastName().equalsIgnoreCase(lastName));
    }
}
