package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataWrapper;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

@Repository
public class JsonDataLoader {
    private final DataWrapper data;

    public JsonDataLoader() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json")) {
            if (inputStream == null) {
                throw new RuntimeException("❌ ERREUR : Impossible de charger le fichier data.json !");
            }
            System.out.println("✅ data.json trouvé, chargement des données...");

            // Désérialisation complète du JSON
            this.data = objectMapper.readValue(inputStream, DataWrapper.class);

            if (data.getPersons() == null || data.getFirestations() == null || data.getMedicalrecords() == null) {
                throw new RuntimeException("❌ ERREUR : Certaines données sont manquantes dans data.json !");
            }

            System.out.println("✅ Données JSON chargées avec succès !");
        } catch (Exception e) {
            throw new RuntimeException("❌ Erreur lors du chargement des données JSON", e);
        }
    }

    public DataWrapper getData() {
        return data;
    }
}
