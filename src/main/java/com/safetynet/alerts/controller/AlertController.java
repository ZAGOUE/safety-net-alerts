package com.safetynet.alerts.controller;


import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.FirestationCoverageDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<FirestationCoverageDTO> getPersonsByStation(@RequestParam int stationNumber) {
        logger.info("📥 Requête GET - Liste des habitants couverts par la station {}", stationNumber);
        return ResponseEntity.ok((FirestationCoverageDTO) firestationService.getPersonsByStation(stationNumber));
    }



    @GetMapping("/childAlert")
    public ResponseEntity<ChildAlertDTO> getChildrenAtAddress(@RequestParam String address) {
        logger.info("📥 Requête GET - Liste des enfants vivant à {}", address);
        return ResponseEntity.ok((ChildAlertDTO) personService.getChildrenByAddress(address));
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
    public ResponseEntity<FireDTO> getFireInfoByAddress(@RequestParam String address) {
        logger.info("📥 Requête GET - Informations sur l'incendie à {}", address);
        return ResponseEntity.ok((FireDTO) firestationService.getFireInfoByAddress(address));
    }


    @GetMapping("/flood/stations")
    public ResponseEntity<Map<String, List<PersonDTO>>> getFloodStations(@RequestParam List<Integer> stations) {
        logger.info("📥 Requête GET - Inondation pour stations: {}", stations);
        return ResponseEntity.ok(firestationService.getFloodStations(stations));
    }



    @GetMapping("/personInfolastName")
    public ResponseEntity<List<PersonDTO>> getPersonInfo(@RequestParam String lastName) {
        logger.info("📥 Requête GET - Informations des personnes avec le nom '{}'", lastName);
        List<PersonDTO> personsInfo = personService.getPersonInfoByLastName(lastName);

        if (personsInfo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personsInfo);
    }




}

