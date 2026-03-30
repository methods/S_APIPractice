package com.example.HearingsDemo.defendantgobaccounts;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DefendantGOBAccountsRepositoryTest {

    @Autowired
    private DefendantGOBAccountsRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ===============================
    // FindByID single lookup - Retrieves one specific, unique account record.
    // The URL: GET /api/v1/defendant-gob-accounts/{masterDefendantId}/{accountCorrelationId}
    // The Method: Optional<DefendantGobAccount> findById(DefendantGobAccountId id)
    // ===============================

    @Test
    @DisplayName("Should find specific, unique account record by Composite Key ids with all fields populated")
    void shouldFindAccountByCompositeId() {
        // 1. Arrange: Define your two IDs
        UUID masterId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();
        DefendantGOBAccountId compositeKey = new DefendantGOBAccountId(masterId, correlationId);

        UUID hearingId = UUID.randomUUID();
        String accountNumber = "0765";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        String caseReferences = "Example Case References";

        String sql = """
            INSERT INTO defendant_gob_accounts (
                master_defendant_id,
                account_correlation_id,
                hearing_id,
                account_number,
                account_request_time,
                case_references,
                created_time,
                updated_time
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
            masterId,
            correlationId,
            hearingId,
            accountNumber,
            now,                 // account_request_time
            caseReferences,
            now,                 // created_time
            now                  // updated_time
            );

        // Act
        Optional<DefendantGOBAccount> result = repository.findById(compositeKey);

        // 3. Assert
        assertThat(result).isPresent().hasValueSatisfying(account -> {
            assertThat(account.getId().getMasterDefendantId()).isEqualTo(masterId);
            assertThat(account.getId().getAccountCorrelationId()).isEqualTo(correlationId);

            assertThat(account.getHearingId()).isEqualTo(hearingId);
            assertThat(account.getAccountNumber()).isEqualTo("0765");

            // Timestamp check
            assertThat(account.getCreatedTime().toString()).startsWith(now.toString().substring(0, 19));
        });
    }

    @Test
    @DisplayName("Should handle NULL values for optional fields in DefendantGOBAccount")
    void shouldFindDefendantGOBAccount_whenOptionalFieldsAreNull() {
        // Arrange:
        UUID masterId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();
        DefendantGOBAccountId compositeKey = new DefendantGOBAccountId(masterId, correlationId);

        UUID hearingId = UUID.randomUUID();
        String caseReferences = "Example Case References";
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();

        String sql = """
            INSERT INTO defendant_gob_accounts (
                master_defendant_id,
                account_correlation_id,
                hearing_id,
                account_number,
                account_request_time,
                case_references,
                created_time,
                updated_time
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
            masterId,
            correlationId,
            hearingId,
            null,
            null,
            caseReferences,
            createdTime,
            updatedTime
        );

        // Act
        Optional<DefendantGOBAccount> result = repository.findById(compositeKey);

        // Assert
        assertThat(result).isPresent().hasValueSatisfying(account -> {
            // Primary Key / Identifiers
            assertThat(account.getId().getMasterDefendantId()).isEqualTo(masterId);
            assertThat(account.getId().getAccountCorrelationId()).isEqualTo(correlationId);
            assertThat(account.getHearingId()).isEqualTo(hearingId);

            // Strings
            assertThat(account.getAccountNumber()).isNull();
            assertThat(account.getCaseReferences()).isEqualTo("Example Case References");

            // Timestamps (Using toString() match up to the second to avoid nano-mismatches)
            assertThat(account.getAccountRequestTime()).isNull();
            assertThat(account.getCreatedTime().toString()).startsWith(createdTime.toString().substring(0, 19));
            assertThat(account.getUpdatedTime().toString()).startsWith(updatedTime.toString().substring(0, 19));
        });

    }


    @Test
    @DisplayName("findById should return an empty Optional when the Composite Key Id is not in the database")
    void shouldReturnEmptyOptional_whenIdDoesNotExist() {

        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();

        DefendantGOBAccountId compositeKey = new DefendantGOBAccountId(masterId, correlationId);

        // Act
        Optional<DefendantGOBAccount> result = repository.findById(compositeKey);

        assertThat(result).isEmpty();
    }

    // TODO
    // =======================================================
    // FindALLByID_findByMasterDefendantId collections lookup
    // The URL: GET /api/v1/defendant-gob-accounts?masterDefendantId={uuid}
    // Using the Index URL version: GET /api/v1/defendant-gob-accounts?masterDefendantId={uuid}&hearingId={uuid}
    // The Why: This returns all accounts belonging to one defendant.
    // The Method: List<DefendantGobAccount> findAllByMasterDefendantId(UUID masterDefendantId)
    // =======================================================

}