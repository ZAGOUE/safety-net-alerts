package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        logger.info("🔍 Récupération de toutes les personnes...");
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{firstName}/{lastName}")
    public ResponseEntity<Person> getPersonByName(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("📥 Requête GET - Recherche de la personne : {} {}", firstName, lastName);
        Optional<Person> person = personService.getPersonByName(firstName, lastName);
        return person.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        logger.info("📥 Requête POST - Ajout d'une nouvelle personne : {} {}", person.getFirstName(), person.getLastName());
        if (personService.addPerson(person)) {
            return ResponseEntity.ok("✅ Personne ajoutée avec succès.");
        } else {
            return ResponseEntity.badRequest().body("❌ La personne existe déjà.");
        }
    }

    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> updatePerson(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Person updatedPerson) {
        logger.info("📥 Requête PUT - Mise à jour de la personne : {} {}", firstName, lastName);
        if (personService.updatePerson(firstName, lastName, updatedPerson)) {
            return ResponseEntity.ok("✅ Mise à jour réussie.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("📥 Requête DELETE - Suppression de la personne : {} {}", firstName, lastName);
        if (personService.deletePerson(firstName, lastName)) {
            return ResponseEntity.ok("✅ Suppression réussie.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
