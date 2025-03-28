package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MedicalRecordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(medicalRecordController).build();
    }

    @Test
    void testGetAllMedicalRecords() throws Exception {
        List<MedicalRecord> medicalRecords = List.of(new MedicalRecord("John", "Doe", "01/01/1990",
                List.of("med1", "med2"), List.of("allergy1")));

        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecords);

        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].birthdate").value("01/01/1990"))
                .andExpect(jsonPath("$[0].medications[0]").value("med1"))
                .andExpect(jsonPath("$[0].allergies[0]").value("allergy1"));

        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

    @Test
    void testGetMedicalRecordByName_Found() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990",
                List.of("med1"), List.of("allergy1"));

        when(medicalRecordService.getMedicalRecordByName("John", "Doe")).thenReturn(Optional.of(medicalRecord));

        mockMvc.perform(get("/medicalRecord/John/Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthdate").value("01/01/1990"))
                .andExpect(jsonPath("$.medications[0]").value("med1"))
                .andExpect(jsonPath("$.allergies[0]").value("allergy1"));

        verify(medicalRecordService, times(1)).getMedicalRecordByName("John", "Doe");
    }

    @Test
    void testGetMedicalRecordByName_NotFound() throws Exception {
        when(medicalRecordService.getMedicalRecordByName("Unknown", "Person")).thenReturn(Optional.empty());

        mockMvc.perform(get("/medicalRecord/Unknown/Person"))
                .andExpect(status().isNotFound());

        verify(medicalRecordService, times(1)).getMedicalRecordByName("Unknown", "Person");
    }

    @Test
    void testAddMedicalRecord_Success() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990",
                List.of("med1"), List.of("allergy1"));

        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(true);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthdate\":\"01/01/1990\",\"medications\":[\"med1\"],\"allergies\":[\"allergy1\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical ajouté avec succès."));

        verify(medicalRecordService, times(1)).addMedicalRecord(any(MedicalRecord.class));
    }

    @Test
    void testAddMedicalRecord_AlreadyExists() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990",
                List.of("med1"), List.of("allergy1"));

        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(false);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthdate\":\"01/01/1990\",\"medications\":[\"med1\"],\"allergies\":[\"allergy1\"]}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Un dossier médical existe déjà."));

        verify(medicalRecordService, times(1)).addMedicalRecord(any(MedicalRecord.class));
    }

    @Test
    void testDeleteMedicalRecord_Success() throws Exception {
        when(medicalRecordService.deleteMedicalRecord("John", "Doe")).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord/John/Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Suppression réussie."));

        verify(medicalRecordService, times(1)).deleteMedicalRecord("John", "Doe");
    }

    @Test
    void testDeleteMedicalRecord_NotFound() throws Exception {
        when(medicalRecordService.deleteMedicalRecord("Unknown", "Person")).thenReturn(false);

        mockMvc.perform(delete("/medicalRecord/Unknown/Person"))
                .andExpect(status().isNotFound());

        verify(medicalRecordService, times(1)).deleteMedicalRecord("Unknown", "Person");
    }
}
