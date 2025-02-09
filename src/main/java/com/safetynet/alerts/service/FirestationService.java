package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.JsonDataLoader;

import java.util.List;
import java.util.Optional;

public class FirestationService {
    private final List<Firestation> firestations;

    public FirestationService(JsonDataLoader jsonDataLoader) {
        this.firestations = jsonDataLoader.getData().getFirestations();
    }

    public List<Firestation> getAllFirestations() {
        return firestations;
    }

    public Optional<Firestation> getFirestation(String address) {
        return firestations.stream().filter(f -> f.getAddress().equalsIgnoreCase(address)).findFirst();
    }

    public boolean addFirestation(Firestation firestation) {
        return firestations.add(firestation);
    }

    public boolean updateFirestation(String address, int stationNumber) {
        return getFirestation(address).map(f -> {
            f.setStation(stationNumber);
            return true;
        }).orElse(false);
    }

    public boolean deleteFirestation(String address) {
        return firestations.removeIf(f -> f.getAddress().equalsIgnoreCase(address));
    }
}
