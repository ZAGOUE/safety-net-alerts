package com.safetynet.alerts.controller;


import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alert")
public class AlertController {
    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private final FirestationService firestationService;
    private final PersonService personService;

    public AlertController(FirestationService firestationService, PersonService personService) {
        this.firestationService = firestationService;
        this.personService = personService;
    }

    @GetMapping("/firestation")
    public ResponseEntity<Map<String, Object>> getPersonsByStation(@RequestParam int stationNumber) {
        logger.info("📥 Requête GET - Recherche des personnes couvertes par la caserne n°{}", stationNumber);
        return ResponseEntity.ok(firestationService.getPersonsByStation(stationNumber));
    }


    @GetMapping("/childAlert")
    public ResponseEntity<Map<String, Object>> getChildrenAtAddress(@RequestParam String address) {
        logger.info("📥 Requête GET - Enfants habitant à {}", address);
        Map<String, Object> response = personService.getChildrenByAddress(address);

        if (((List<?>) response.get("children")).isEmpty()) {
            return ResponseEntity.ok(Collections.emptyMap());
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneNumbersByStation(@RequestParam int firestation) {
        logger.info("📥 Requête GET - Liste des numéros de téléphone pour la caserne n°{}", firestation);
        return ResponseEntity.ok(firestationService.getPhoneNumbersByStation(firestation));
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getCommunityEmails(@RequestParam String city) {
        List<String> emails = personService.getCommunityEmails(city);
        return emails.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(emails);
    }

    @GetMapping("/fire")
    public ResponseEntity<Map<String, Object>> getFireAlert(@RequestParam String address) {
        logger.info("📥 Requête GET - Récupération des habitants et de la caserne pour l'adresse : {}", address);
        Map<String, Object> result = firestationService.getFireAlertByAddress(address);
        return result.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getFloodStations(@RequestParam List<Integer> stations) {
        logger.info("📥 Requête GET - Récupération des foyers desservis par les stations : {}", stations);
        Map<String, List<Map<String, Object>>> result = firestationService.getFloodStations(stations);
        return result.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }

    @GetMapping("/personInfolastName")
    public ResponseEntity<List<Map<String, Object>>> getPersonInfo(@RequestParam String lastName) {
        logger.info("📥 Requête GET - Informations des personnes avec le nom '{}'", lastName);
        List<Map<String, Object>> personsInfo = personService.getPersonInfoByLastName(lastName);

        if (personsInfo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personsInfo);
    }




}

