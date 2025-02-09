package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.JsonDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FirestationService {
    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);
    private final List<Firestation> firestations;

    public FirestationService(JsonDataLoader jsonDataLoader) {
        this.firestations = jsonDataLoader.getData().getFirestations();
        logger.info("✅ Chargement des données des casernes terminé.");
    }

    public List<Firestation> getAllFirestations() {
        logger.debug("🔍 Récupération de toutes les casernes de pompiers.");
        return firestations;
    }

    public Optional<Firestation> getFirestationByAddress(String address) {
        logger.debug("🔍 Recherche de la caserne desservant l'adresse : {}", address);
        return firestations.stream()
                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }

    public boolean addFirestation(Firestation firestation) {
        if (getFirestationByAddress(firestation.getAddress()).isPresent()) {
            logger.error("❌ Échec : Une caserne dessert déjà cette adresse ({})", firestation.getAddress());
            return false;
        }
        firestations.add(firestation);
        logger.info("✅ Nouvelle caserne ajoutée : Station {} pour l'adresse {}", firestation.getStation(), firestation.getAddress());
        return true;
    }

    public boolean updateFirestation(String address, Firestation updatedFirestation) {
        Optional<Firestation> firestationOpt = getFirestationByAddress(address);
        if (firestationOpt.isPresent()) {
            Firestation firestation = firestationOpt.get();
            logger.debug("🔄 Mise à jour de la caserne à l'adresse {}", address);
            firestation.setStation(updatedFirestation.getStation());
            logger.info("✅ Mise à jour réussie pour l'adresse {}", address);
            return true;
        } else {
            logger.error("❌ Échec : Aucune caserne ne dessert l'adresse {}", address);
            return false;
        }
    }

    public boolean deleteFirestation(String address) {
        if (firestations.removeIf(firestation -> firestation.getAddress().equalsIgnoreCase(address))) {
            logger.info("🗑️ Suppression réussie de la caserne à l'adresse {}", address);
            return true;
        } else {
            logger.error("❌ Échec : Impossible de supprimer la caserne, adresse non trouvée.");
            return false;
        }
    }
}
