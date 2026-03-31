package com.example.HearingsDemo.defendantgobaccounts;


import com.example.HearingsDemo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
    private DefendantGOBAccount createEntity(UUID masterId, UUID correlationId) {

        // Create the composit key
        DefendantGOBAccountId id = new DefendantGOBAccountId(masterId, correlationId);

        UUID hearingId = UUID.randomUUID();
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
    void shouldReturnDto_whenRecordExists() {
        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();

        DefendantGOBAccountId compositeKey = new DefendantGOBAccountId(masterId, correlationId);

        // Create fake entities
        DefendantGOBAccount mockEntity = createEntity(masterId, correlationId);

        when(repository.findById(compositeKey)).thenReturn(Optional.of(mockEntity));

        // --- ACT ---
        DefendantGOBAccountDTO result = service.getAccountByIds(masterId, correlationId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.masterDefendantId()).isEqualTo(masterId);
        assertThat(result.accountCorrelationId()).isEqualTo(correlationId);
    }

    @Test
    @DisplayName("getAccountById should throw ResourceNotFoundException when no account is found")
    void getAccountById_shouldThrowException_whenNoAccountIsFound() {

        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();
        DefendantGOBAccountId compositeKey = new DefendantGOBAccountId(masterId, correlationId);

        when(repository.findById(compositeKey)).thenReturn(Optional.empty());

        // Act & Assert (We do them together for exceptions)
        assertThatThrownBy(() -> service.getAccountByIds(masterId, correlationId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Account not found for the given IDs");

    }
}