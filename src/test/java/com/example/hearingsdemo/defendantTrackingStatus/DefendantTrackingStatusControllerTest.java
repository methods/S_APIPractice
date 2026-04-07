package com.example.hearingsdemo.defendantTrackingStatus;


import com.example.hearingsdemo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DefendantTrackingStatusController.class)
public class DefendantTrackingStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DefendantTrackingStatusService service;

    @Test
    @DisplayName("GET /api/v1/offences/{offenceId}/tracking-status should return 200 OK with a JSON with tracking " +
        "status when found")
    void shouldReturn200AndJson_whenOffenceExists() throws Exception {

        // Arrange
        UUID offenceId = UUID.randomUUID();
        UUID defendantId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // Create mockDTO
        DefendantTrackingStatusDTO mockDTO = new DefendantTrackingStatusDTO(
            offenceId,
            defendantId,
            true,
            now,
            true,
            now
        );

        when(service.getDefendantTrackingStatusByOffenceId(offenceId)).thenReturn(mockDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/offences/{offenceId}/tracking-status", offenceId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.offenceId").value(offenceId.toString()))
            .andExpect(jsonPath("$.defendantId").value(defendantId.toString()))
            .andExpect(jsonPath("$.emStatus").value(true))
            .andExpect(jsonPath("$.emLastModifiedTime").exists())
            .andExpect(jsonPath("$.woaStatus").value(true))
            .andExpect(jsonPath("$.woaLastModifiedTime").exists());

    }

    // --- Empty Case Test ---
    @Test
    @DisplayName("GET /api/v1/offences/{offenceId}/tracking-status should return 404 ResourceNotFoundException when " +
        "tracking status is not found" +
        " found")
    void shouldReturnResourceNotFoundErrorWithCustomMessageWhenNotFound() throws Exception {
        //Arrange
        UUID offenceId = UUID.randomUUID();

        when(service.getDefendantTrackingStatusByOffenceId(offenceId))
            .thenThrow(new ResourceNotFoundException(
                "Tracking status not found for ID: " + offenceId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/offences/{offenceId}/tracking-status", offenceId))
            .andExpect(status().isNotFound());
    }
}