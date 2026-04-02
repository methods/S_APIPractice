package com.example.HearingsDemo.defendantgobaccounts;


import com.example.HearingsDemo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefendantGOBAccountsServiceTest {

    @Mock
    private DefendantGOBAccountsRepository repository;

    @InjectMocks
    private DefendantGOBAccountsService service;

    // ========================================================================
    // A simple utility method for creating test DefendantGOBAccounts entities
    // ========================================================================
    private DefendantGOBAccount createEntity(UUID masterId, UUID correlationId, UUID hearingId) {

        // Create the composite key
        DefendantGOBAccountId id = new DefendantGOBAccountId(masterId, correlationId);

        LocalDateTime now = LocalDateTime.now();

        return new DefendantGOBAccount(
            id,                   // The Composite Key
            hearingId,            // hearing_id
            "0765",               // account_number
            now,                  // account_request_time
            "Case-Ref-123",       // case_references
            now,                  // created_time
            now                   // updated_time
        );
    }

    // ======================================================================
    // Single lookup - findById( composite key )
    // ======================================================================
    @Test
    @DisplayName("FindById should return mapped DTO when record is found")
    void shouldReturnDtoWhenRecordExists() {
        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();

        DefendantGOBAccountId compositeKey = new DefendantGOBAccountId(masterId, correlationId);

        // Create fake entities
        DefendantGOBAccount mockEntity = createEntity(masterId, correlationId, hearingId);

        when(repository.findById(compositeKey)).thenReturn(Optional.of(mockEntity));

        // --- ACT ---
        DefendantGOBAccountDTO result = service.getAccountByIds(masterId, correlationId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.masterDefendantId()).isEqualTo(masterId);
        assertThat(result.accountCorrelationId()).isEqualTo(correlationId);
    }

    @Test
    @DisplayName("getAccountByIds should throw ResourceNotFoundException when no account is found")
    void getAccountByIdShouldThrowExceptionWhenNoAccountIsFound() {

        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();
        DefendantGOBAccountId compositeKey = new DefendantGOBAccountId(masterId, correlationId);

        when(repository.findById(compositeKey)).thenReturn(Optional.empty());

        // Act & Assert (We do them together for exceptions)
        assertThatThrownBy(() -> service.getAccountByIds(masterId, correlationId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Account not found for masterDefendantId=" + masterId
                + ", accountCorrelationId=" + correlationId);

    }


    // =======================================================
    // Index Collections lookup
    // =======================================================

    @Test
    @DisplayName("getAllAccountsForHearing should return a mapped list of account DTOs when found")
    void getAllAccountsByMasterIdAndHearingIdShouldReturnMappedDtosWhenAccountsFound() {

        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();

        UUID correlationId1 = UUID.randomUUID();
        UUID correlationId2 = UUID.randomUUID();

        // Create fake Entities
        DefendantGOBAccount entity1 = createEntity(masterId, correlationId1, hearingId);
        DefendantGOBAccount entity2 = createEntity(masterId, correlationId2, hearingId);

        when(repository.findAllById_MasterDefendantIdAndHearingId(masterId, hearingId))
            .thenReturn(List.of(entity1, entity2));

        // Act
        List<DefendantGOBAccountDTO> results = service.getAllAccountsForHearing(masterId,
            hearingId);

        // Assert
        assertThat(results)
            .hasSize(2)
            .allSatisfy(dto -> {
                assertThat(dto.masterDefendantId()).isEqualTo(masterId);
                assertThat(dto.hearingId()).isEqualTo(hearingId);
                assertThat(dto.accountNumber()).isEqualTo("0765");
            });

        // Check that the two unique correlation IDs are present in the list
        assertThat(results)
            .extracting(DefendantGOBAccountDTO::accountCorrelationId)
            .containsExactlyInAnyOrder(correlationId1, correlationId2);
    }

    @Test
    @DisplayName("getAllAccountsForHearing should return an empty list when no documents are found")
    void getAllAccountsByMasterIdAndHearingIdShouldReturnEmptyListWhenNoAccountsFound() {
        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();

        when(repository.findAllById_MasterDefendantIdAndHearingId(masterId, hearingId)).thenReturn(Collections.emptyList());

        // Act
        List<DefendantGOBAccountDTO> results = service.getAllAccountsForHearing(masterId,
            hearingId);

        // Assert
        assertThat(results).isEmpty();
    }
}