package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
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
}

