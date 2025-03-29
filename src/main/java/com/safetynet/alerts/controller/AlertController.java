package com.safetynet.alerts.controller;




import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Map<String, Object> result = firestationService.getPersonsByStation(stationNumber);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/childAlert")
    public ResponseEntity<Map<String, Object>> getChildrenByAddress(@RequestParam String address) {
        logger.info("Requête GET - Recherche des enfants à l'adresse : {}", address);
        Map<String, Object> result = firestationService.getChildrenByAddress(address);
        if (result == null || ((List<?>) result.get("children")).isEmpty()) {
            logger.warn("Aucun enfant trouvé à l'adresse : {}", address);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Aucun enfant trouvé à cette adresse."));
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneNumbersByStation(@RequestParam int firestation) {
        logger.info("Requête GET - Liste des numéros de téléphone pour la caserne n°{}", firestation);
        return ResponseEntity.ok(firestationService.getPhoneNumbersByStation(firestation));
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getCommunityEmails(@RequestParam String city) {
        Set<String> emails = personService.getCommunityEmails(city);
        return emails.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(new ArrayList<>(emails));
    }

    @GetMapping("/fire")
    public ResponseEntity<FireDTO> getFireInfoByAddress(@RequestParam String address) {
        logger.info("Requête GET - Informations sur l'incendie à {}", address);
        return ResponseEntity.ok(firestationService.getFireInfoByAddress(address)); // Retourne un `FireDTO`
    }



    @GetMapping("/flood/stations")
    public ResponseEntity<Map<String, List<PersonDTO>>> getFloodStations(@RequestParam List<Integer> stations) {
        logger.info("Requête GET - caserne en alerte: {}", stations);
        return ResponseEntity.ok(firestationService.getFloodStations(stations));
    }


    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfoByLastName(@RequestParam String lastName) {
        logger.info("Requête GET - Recherche d'informations pour {}", lastName);
        List<PersonInfoDTO> result = personService.getPersonInfoByLastName(lastName);
        if (result.isEmpty()) {
            logger.warn("Aucun résident trouvé avec le nom {}", lastName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(result);
    }





}

