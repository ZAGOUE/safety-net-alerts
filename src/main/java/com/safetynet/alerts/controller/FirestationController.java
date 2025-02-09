package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/firestation")
public class FirestationController {
    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping
    public List<Firestation> getAllFirestations() {
        return firestationService.getAllFirestations();
    }

    @PostMapping
    public ResponseEntity<String> addFirestation(@RequestBody Firestation firestation) {
        return firestationService.addFirestation(firestation) ?
                ResponseEntity.ok("Firestation added successfully") :
                ResponseEntity.badRequest().body("Failed to add firestation");
    }

    @PutMapping("/{address}")
    public ResponseEntity<String> updateFirestation(@PathVariable String address, @RequestParam int stationNumber) {
        return firestationService.updateFirestation(address, stationNumber) ?
                ResponseEntity.ok("Firestation updated successfully") :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{address}")
    public ResponseEntity<String> deleteFirestation(@PathVariable String address) {
        return firestationService.deleteFirestation(address) ?
                ResponseEntity.ok("Firestation deleted successfully") :
                ResponseEntity.notFound().build();
    }
}

