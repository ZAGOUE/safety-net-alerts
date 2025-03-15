package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/firestation")
public class FirestationController {
    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);
    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping
    public ResponseEntity<List<Firestation>> getAllFirestations() {
        logger.info("Requête GET - Récupération de toutes les casernes.");
        return ResponseEntity.ok(firestationService.getAllFirestations());
    }

    @GetMapping("/{address}")
    public ResponseEntity<Firestation> getFirestationByAddress(@PathVariable String address) {
        logger.info("Requête GET - Recherche de la caserne pour l'adresse : {}", address);
        Optional<Firestation> firestation = Optional.ofNullable(firestationService.getFirestationByAddress(address));
        return firestation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{address}")
    public ResponseEntity<String> updateFirestation(@PathVariable String address, @RequestBody Firestation updatedFirestation) {
        logger.info("Requête PUT - Mise à jour de la caserne pour {}", address);
        if (firestationService.updateFirestation(address, updatedFirestation)) {
            return ResponseEntity.ok("Mise à jour réussie.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
