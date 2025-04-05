package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.alerts.model.DataWrapper;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class JsonDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataLoader.class);

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private String filePath;

    @Getter
    private DataWrapper data;

    // Constructeur avec valeur par défaut
    public JsonDataLoader(@Value("${data.file.path:src/main/resources/data.json}") String filePath) {
        this.filePath = filePath;
        loadData();
    }


    private void loadData() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("ERREUR : Fichier introuvable : " + filePath);
            }
            this.data = objectMapper.readValue(file, DataWrapper.class);
            logger.info("Données chargées depuis : {}", filePath);

        } catch (IOException e) {
            logger.error("Erreur lors du chargement des données JSON", e);
            throw new RuntimeException("Erreur lors du chargement des données JSON", e);
        }
    }

    private void saveData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
        } catch (IOException e) {
            logger.error("Erreur lors de la sauvegarde du fichier JSON", e);
        }
    }
    public void saveAllMedicalRecords(List<MedicalRecord> medicalRecords) {
        data.setMedicalrecords(medicalRecords);
        saveData();
    }


    public void saveAllPersons(List<Person> persons) {
        data.setPersons(persons);
        saveData();
    }
    public void saveAllFirestations(List<Firestation> firestations) {
        data.setFirestations(firestations);
        saveData();
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
    // Constructeur sans argument
    public JsonDataLoader() {
        this("src/main/resources/data.json");
    }

}
