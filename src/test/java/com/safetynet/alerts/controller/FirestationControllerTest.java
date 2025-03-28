package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FirestationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FirestationService firestationService;

    @InjectMocks
    private FirestationController firestationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(firestationController).build();
    }

    @Test
    void testGetAllFirestations() throws Exception {
        List<Firestation> firestations = List.of(new Firestation("123 Street", 1));
        when(firestationService.getAllFirestations()).thenReturn(firestations);

        mockMvc.perform(get("/firestation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].address").value("123 Street"))
                .andExpect(jsonPath("$[0].station").value(1));

        verify(firestationService, times(1)).getAllFirestations();
    }

    @Test
    void testGetFirestationByAddress_Found() throws Exception {
        Firestation firestation = new Firestation("123 Street", 1);
        when(firestationService.getFirestationByAddress("123 Street")).thenReturn(firestation);

        mockMvc.perform(get("/firestation/123 Street"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("123 Street"))
                .andExpect(jsonPath("$.station").value(1));

        verify(firestationService, times(1)).getFirestationByAddress("123 Street");
    }

    @Test
    void testGetFirestationByAddress_NotFound() throws Exception {
        when(firestationService.getFirestationByAddress("Unknown Address")).thenReturn(null);

        mockMvc.perform(get("/firestation/Unknown Address"))
                .andExpect(status().isNotFound());

        verify(firestationService, times(1)).getFirestationByAddress("Unknown Address");
    }

    @Test
    void testUpdateFirestation_Success() throws Exception {
        Firestation updatedFirestation = new Firestation("123 Street", 2);
        when(firestationService.updateFirestation(eq("123 Street"), any(Firestation.class))).thenReturn(true);

        mockMvc.perform(put("/firestation/123 Street")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"123 Street\", \"station\":2}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mise à jour réussie."));

        verify(firestationService, times(1)).updateFirestation(eq("123 Street"), any(Firestation.class));
    }

    @Test
    void testUpdateFirestation_NotFound() throws Exception {
        Firestation updatedFirestation = new Firestation("Unknown Address", 3);
        when(firestationService.updateFirestation(eq("Unknown Address"), any(Firestation.class))).thenReturn(false);

        mockMvc.perform(put("/firestation/Unknown Address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"Unknown Address\", \"station\":3}"))
                .andExpect(status().isNotFound());

        verify(firestationService, times(1)).updateFirestation(eq("Unknown Address"), any(Firestation.class));
    }
}
