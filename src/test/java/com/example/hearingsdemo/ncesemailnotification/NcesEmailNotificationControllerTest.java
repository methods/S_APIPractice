package com.example.hearingsdemo.ncesemailnotification;


import com.example.hearingsdemo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(NcesEmailNotificationController.class)
public class NcesEmailNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NcesEmailNotificationService service;

    @Test
    @DisplayName("GET /api/v1/nces-notifications/{id} - Should return 200 OK and JSON representation when the record exists")
    void shouldReturn200AndDto_whenIdExists() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();

        // Create a mockDto
        NcesEmailNotificationDTO mockDto = new NcesEmailNotificationDTO(
            id,
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Test Recipient",
            "Test Subject"
        );

        when(service.getNotificationById(id)).thenReturn(mockDto);

        // Act & assert
        mockMvc.perform(get("/api/v1/nces-notifications/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Identifiers (Must use .toString() for UUID comparison)
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.materialId").value(mockDto.materialId().toString()))
            .andExpect(jsonPath("$.notificationId").value(mockDto.notificationId().toString()))
            .andExpect(jsonPath("$.masterDefendantId").value(mockDto.masterDefendantId().toString()))

            // Strings / Data (Referencing the DTO directly for stability)
            .andExpect(jsonPath("$.sendTo").value(mockDto.sendTo()))
            .andExpect(jsonPath("$.subject").value(mockDto.subject()));
    }

    @Test
    @DisplayName("GET /api/v1/nces-notifications/{id} - Should return 404 Not Found when the service throws ResourceNotFoundException")
    void shouldReturn404_whenNotFound() throws Exception {
        //Arrange
        UUID id = UUID.randomUUID();

        when(service.getNotificationById(id))
            .thenThrow(new ResourceNotFoundException(
                "Notification not found for id: " + id));

        // Act & Assert
        mockMvc.perform(get("/api/v1/nces-notifications/{id}", id))
            .andExpect(status().isNotFound());
    }
}