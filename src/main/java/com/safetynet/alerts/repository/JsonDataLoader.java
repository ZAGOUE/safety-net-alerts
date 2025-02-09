package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataWrapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.List;

@Repository
public class JsonDataLoader {
    private final List<Person> persons;
    private final List<Firestation> firestations;
    private final List<MedicalRecord> medicalrecords;

    public JsonDataLoader() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json")) {
            if (inputStream == null) {
                throw new RuntimeException("❌ ERREUR : Impossible de charger le fichier data.json !");
            }
            System.out.println("✅ data.json trouvé, chargement des données...");

            // Désérialisation complète du JSON
            DataWrapper data = objectMapper.readValue(inputStream, DataWrapper.class);

            this.persons = data.getPersons();
            this.firestations = data.getFirestations();
            this.medicalrecords = data.getMedicalrecords();

            if (persons == null || firestations == null || medicalrecords == null) {
                throw new RuntimeException("❌ ERREUR : Certaines données sont manquantes dans data.json !");
            }

            System.out.println("✅ Données JSON chargées avec succès !");
        } catch (Exception e) {
            throw new RuntimeException("❌ Erreur lors du chargement des données JSON", e);
        }
    }

    public List<Person> getAllPersons() { return persons; }
    public List<Firestation> getAllFirestations() { return firestations; }
    public List<MedicalRecord> getAllMedicalRecords() { return medicalrecords; }
}
