package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(AlertController.class)
@Import(AlertController.class)
@ContextConfiguration(classes = AlertController.class)
class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FirestationService firestationService;

    @MockitoBean
    private PersonService personService;

    @Test
    void testGetPersonsByStation() throws Exception {
        PersonDTO personDTO = new PersonDTO(new Person("John", "Doe", "123 Street", "City", "12345", "123456789", "email@example.com", "01/01/1990"),
                new MedicalRecord("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1")));

        Map<String, Object> response = Map.of(
                "adults", 2,
                "children", 1,
                "persons", List.of(personDTO)
        );

        when(firestationService.getPersonsByStation(1)).thenReturn(response);

        mockMvc.perform(get("/alert/firestation?stationNumber=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adults").value(2))
                .andExpect(jsonPath("$.children").value(1))
                .andExpect(jsonPath("$.persons[0].firstName").value("John"));
    }


    @Test
    void testGetChildrenByAddress_NotFound() throws Exception {
        when(firestationService.getChildrenByAddress("123 Street")).thenReturn(Map.of("children", List.of()));

        mockMvc.perform(get("/alert/childAlert?address=123 Street"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Aucun enfant trouvé à cette adresse."));
    }


    @Test
    void testGetPhoneNumbersByStation() throws Exception {
        when(firestationService.getPhoneNumbersByStation(1)).thenReturn(List.of("123-456-7890", "987-654-3210"));

        mockMvc.perform(get("/alert/phoneAlert?firestation=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("123-456-7890"))
                .andExpect(jsonPath("$[1]").value("987-654-3210"));
    }


    @Test
    void testGetCommunityEmails() throws Exception {
        when(personService.getCommunityEmails("Paris")).thenReturn(Set.of("test@example.com"));

        mockMvc.perform(get("/alert/communityEmail?city=Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("test@example.com"));
    }


    @Test
    void testGetFireInfoByAddress() throws Exception {
        // Création d'un `PersonDTO` valide
        Person person = new Person("John", "Doe", "456 Street", "City", "12345", "123456789", "email@example.com", "01/01/1990");
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"));
        PersonDTO personDTO = new PersonDTO(person, medicalRecord);

        FireDTO fireDTO = new FireDTO(
                List.of(personDTO), // Liste des résidents
                2 // Numéro de la caserne associée
        );

        // Retourner un "FireDTO"
        when(firestationService.getFireInfoByAddress("456 Street")).thenReturn(fireDTO);

        // Exécution du test avec MockMvc et vérification de la réponse
        mockMvc.perform(get("/alert/fire?address=456 Street"))
                .andExpect(status().isOk()) // status 200
                .andExpect(jsonPath("$.residents[0].firstName").value("John"))
                .andExpect(jsonPath("$.residents[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.stationNumber").value(2));
    }


    @Test
    void testGetFloodStations() throws Exception {
        // Création d'un `Person` valide
        Person person = new Person("John", "Doe", "123 Street", "City", "12345", "123456789", "email@example.com", "01/01/1990");

        // Création d'un `MedicalRecord` valide
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"));

        // Création d'un `PersonDTO`
        PersonDTO personDTO = new PersonDTO(person, medicalRecord);

        // Simuler le retour de `firestationService.getFloodStations(...)`
        when(firestationService.getFloodStations(List.of(1, 2)))
                .thenReturn(Map.of("123 Street", List.of(personDTO)));

        // Exécution du test MockMvc et vérification de la réponse
        mockMvc.perform(get("/alert/flood/stations?stations=1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['123 Street'][0].firstName").value("John"))
                .andExpect(jsonPath("$.['123 Street'][0].lastName").value("Doe"));
    }


    @Test
    void testGetPersonInfoByLastName() throws Exception {
        // Création d'un `Person` simulé
        Person person = new Person("John", "Doe", "123 Street", "City", "12345", "123456789", "email@example.com", "01/01/1990");

        // Création d'un `MedicalRecord` simulé
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990",
                List.of("med1", "med2"),
                List.of("allergy1", "allergy2"));

        // Création d'un `PersonInfoDTO` avec les données médicales
        PersonInfoDTO personInfoDTO = new PersonInfoDTO(
                "John",
                "Doe",
                "123 Street",
                "email@example.com",
                30,
                "med1, med2",
                "allergy1, allergy2"
        );

        // Simulation de `personService.getPersonInfoByLastName("Doe")`
        when(personService.getPersonInfoByLastName("Doe")).thenReturn(List.of(personInfoDTO));

        // Exécution du test avec `MockMvc` et vérification des résultats
        mockMvc.perform(get("/alert/personInfo?lastName=Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].address").value("123 Street"))
                .andExpect(jsonPath("$[0].email").value("email@example.com"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[0].medications").value("med1, med2"))
                .andExpect(jsonPath("$[0].allergies").value("allergy1, allergy2"));
    }
}

