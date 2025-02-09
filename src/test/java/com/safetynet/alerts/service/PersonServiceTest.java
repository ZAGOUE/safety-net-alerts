package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {
    private final PersonService personService = new PersonService(new JsonDataLoader());

    @Test
    void testGetAllPersons() {
        List<Person> persons = personService.getAllPersons();
        assertNotNull(persons);
        assertFalse(persons.isEmpty());
    }
}
