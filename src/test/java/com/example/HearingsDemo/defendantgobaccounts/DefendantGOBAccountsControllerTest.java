package com.example.HearingsDemo.defendantgobaccounts;


import com.example.HearingsDemo.exception.ResourceNotFoundException;
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

@WebMvcTest(DefendantGOBAccountsController.class)
public class DefendantGOBAccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DefendantGOBAccountsService service;

    @Test
    @DisplayName("GET /api/v1/defendant-gob-accounts/{masterDefendantId}/correlations/{accountCorrelationId} - Should" +
        " return 200 OK and JSON representation when the account exists")
    void shouldReturn200AndDto_whenAccountExists() throws Exception {
        // Arrange
        UUID masterDefendantId = UUID.randomUUID();
        UUID accountCorrelationId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        LocalDateTime accountRequestTime = LocalDateTime.now();

        // Create  mock dto
        DefendantGOBAccountDTO mockDto = new DefendantGOBAccountDTO(
            masterDefendantId,
            accountCorrelationId,
            hearingId,
            "0745",
            accountRequestTime,
            "TEST Ref"
        );

        when(service.getAccountByIds(masterDefendantId, accountCorrelationId)).thenReturn(mockDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/defendant-gob-accounts/{masterDefendantId}/correlations/{accountCorrelationId}",
                masterDefendantId, accountCorrelationId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Identifiers
            .andExpect(jsonPath("$.masterDefendantId").value(masterDefendantId.toString()))
            .andExpect(jsonPath("$.accountCorrelationId").value(accountCorrelationId.toString()))
            .andExpect(jsonPath("$.hearingId").value(hearingId.toString()))
            .andExpect(jsonPath("$.accountRequestTime").value(accountRequestTime.toString()))
            // String values
            .andExpect(jsonPath("$.accountNumber").value(mockDto.accountNumber()))
            .andExpect(jsonPath("$.caseReferences").value(mockDto.caseReferences()));

    }

    @Test
    @DisplayName("GET /api/v1/defendant-gob-accounts//{masterDefendantId}/correlations/{accountCorrelationId} - Should return 404 Not Found when the service throws ResourceNotFoundException")
    void shouldReturn404_whenNotFound() throws Exception {
        // Arrange
        UUID masterDefendantId = UUID.randomUUID();
        UUID accountCorrelationId = UUID.randomUUID();

        when(service.getAccountByIds(masterDefendantId, accountCorrelationId))
            .thenThrow(new ResourceNotFoundException(
                "Account not found for the given IDs"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/defendant-gob-accounts//{masterDefendantId}/correlations/{accountCorrelationId}",
                masterDefendantId, accountCorrelationId))
            .andExpect(status().isNotFound());

    }
}
