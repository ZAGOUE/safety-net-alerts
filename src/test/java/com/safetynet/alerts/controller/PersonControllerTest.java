package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
@Import(PersonService.class) // ✅ Corrige @MockBean déprécié
@ContextConfiguration(classes = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService; // ✅ Remplace @MockBean

    @InjectMocks
    private PersonController personController; // ✅ Injecte le contrôleur

    @Test
    void testGetPersonByName_Found() throws Exception {
        Person mockPerson = new Person("John", "Doe", "123 Street", "City", "12345", "123456789", "email@example.com", "01/01/1990");
        when(personService.getPersonByName("John", "Doe")).thenReturn(Optional.of(mockPerson));

        mockMvc.perform(get("/person/John/Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testAddPerson_Success() throws Exception {
        Person person = new Person("John", "Doe", "123 Street", "City", "12345", "123456789", "email@example.com", "01/01/1990");
        when(personService.addPerson(any(Person.class))).thenReturn(true);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(content().string("Personne ajoutée avec succès."));
    }

    @Test
    void testDeletePerson_NotFound() throws Exception {
        when(personService.deletePerson("Jane", "Doe")).thenReturn(false);

        mockMvc.perform(delete("/person/Jane/Doe"))
                .andExpect(status().isNotFound());
    }
}
