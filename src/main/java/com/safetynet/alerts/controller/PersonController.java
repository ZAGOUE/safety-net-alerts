package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @PostMapping
    public ResponseEntity<String> addPerson(
            @RequestBody Person person) {
        return personService.addPerson(person) ?
                ResponseEntity.ok("Person added successfully") :
                ResponseEntity.badRequest().body("Failed to add person");
    }

    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> updatePerson(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody Person person) {
        return personService.updatePerson(firstName, lastName, person) ?
                ResponseEntity.ok("Person updated successfully") :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(
            @PathVariable String firstName,
            @PathVariable String lastName) {
        return personService.deletePerson(firstName, lastName) ?
                ResponseEntity.ok("Person deleted successfully") :
                ResponseEntity.notFound().build();
    }
}
