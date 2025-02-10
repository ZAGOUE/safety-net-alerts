package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.alerts.model.DataWrapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.List;

@Repository
public class JsonDataLoader {
    private final DataWrapper data;

    public JsonDataLoader() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 🔥 Ajout du support des LocalDate
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json")) {
            if (inputStream == null) {
                throw new RuntimeException("❌ ERREUR : Impossible de charger le fichier data.json !");
            }
            System.out.println("✅ data.json trouvé, chargement des données...");
            this.data = objectMapper.readValue(inputStream, DataWrapper.class);
            System.out.println("✅ Données JSON chargées avec succès !");
        } catch (Exception e) {
            throw new RuntimeException("❌ Erreur lors du chargement des données JSON", e);
        }
    }

    public DataWrapper getData() {
        return data;
    }

    public List<Person> getAllPersons() {
        return data.getPersons();
    }

    public List<Firestation> getAllFirestations() {
        return data.getFirestations();
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return data.getMedicalrecords();
    }
}
