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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
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
    void shouldReturn200AndDtoWhenAccountExists() throws Exception {
        // Arrange
        UUID masterDefendantId = UUID.randomUUID();
        UUID accountCorrelationId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        // Hardcode the time to a specific value
        LocalDateTime accountRequestTime = LocalDateTime.of(2025, 12, 25, 10, 0, 0);

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
            .andExpect(jsonPath("$.accountRequestTime").value("2025-12-25T10:00:00"))

            // String values
            .andExpect(jsonPath("$.accountNumber").value(mockDto.accountNumber()))
            .andExpect(jsonPath("$.caseReferences").value(mockDto.caseReferences()));

    }

    @Test
    @DisplayName("GET /api/v1/defendant-gob-accounts//{masterDefendantId}/correlations/{accountCorrelationId} - Should return 404 Not Found when the service throws ResourceNotFoundException")
    void shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        UUID masterDefendantId = UUID.randomUUID();
        UUID accountCorrelationId = UUID.randomUUID();

        when(service.getAccountByIds(masterDefendantId, accountCorrelationId))
            .thenThrow(new ResourceNotFoundException(
                "Account not found for the given IDs"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/defendant-gob-accounts/{masterDefendantId}/correlations/{accountCorrelationId}",
                masterDefendantId, accountCorrelationId))
            .andExpect(status().isNotFound());
        verify(service).getAccountByIds(masterDefendantId, accountCorrelationId);


    }

    // =======================================================
    // Index Collections lookup
    // =======================================================

    @Test
    @DisplayName("GET /api/v1/defendant-gob-accounts should return a 200 OK with a list of account dtos associated with a hearingId when found")
    void shouldReturnOKWithDtosWhenFound() throws Exception {
        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        UUID correlationId1 = UUID.randomUUID();
        UUID correlationId2 = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        List<DefendantGOBAccountDTO> mockDtos = List.of(
            new DefendantGOBAccountDTO(masterId, correlationId1, hearingId, "0745", now, "Test Ref 1"),
            new DefendantGOBAccountDTO(masterId, correlationId2, hearingId, "034", now, "Test Ref 2")
        );

        when(service.getAllAccountsForHearing(masterId, hearingId)).thenReturn(mockDtos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/defendant-gob-accounts")
                .param("masterDefendantId", masterId.toString())
                .param("hearingId", hearingId.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].accountNumber").value("0745"))

            .andExpect(jsonPath("$[0].accountCorrelationId").value(correlationId1.toString()))
            // 3. Verify the second element data
            .andExpect(jsonPath("$[1].accountNumber").value("034"))
            .andExpect(jsonPath("$[1].accountCorrelationId").value(correlationId2.toString()));
    }

    @Test
    @DisplayName("GET /api/v1/defendant-gob-accounts should return 200 OK with an empty list when no accounts are " +
        "found")
    void shouldReturnOKWithEmptyListWhenNotFound() throws Exception {

        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();

        when(service.getAllAccountsForHearing(masterId, hearingId)).thenReturn(List.of());

        // Act & assert
        mockMvc.perform(get("/api/v1/defendant-gob-accounts")
                .param("masterDefendantId", masterId.toString())
                .param("hearingId", hearingId.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Check that the root element is empty
            .andExpect(jsonPath("$").isEmpty());

    }
}
