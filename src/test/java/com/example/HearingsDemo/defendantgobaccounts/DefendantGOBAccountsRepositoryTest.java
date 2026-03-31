package com.example.HearingsDemo.defendantgobaccounts;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
    // The URL: GET /api/v1/defendant-gob-accounts/{masterDefendantId}/correlations/{accountCorrelationId}
    // The Method: Optional<DefendantGOBAccount> findById(DefendantGOBAccountId id)
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
            assertThat(account.getCreatedTime()).isEqualTo(now);
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

    // =======================================================
    // Index lookup
    // The URL: GET /api/v1/defendant-gob-accounts?masterDefendantId={uuid}&hearingId={uuid}
    // The Why: Business This returns all accounts belonging to the given defendant and hearingId.
    // The Method: List<DefendantGOBAccount> findAllByMasterIdAndHearingId(UUID masterDefendantId, UUID hearingId)
    // =======================================================


    @Test
    @DisplayName("findAllByMasterIdAndHearingId should find all accounts belonging to a specific Master Defendant and Hearing ID")
    void shouldFindAllAccountsByMasterIdAndHearingId() {

        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();

        UUID correlationId1 = UUID.randomUUID();
        UUID correlationId2 = UUID.randomUUID();

        String sql = """
        INSERT INTO defendant_gob_accounts (
            master_defendant_id,
            account_correlation_id,
            hearing_id,
            account_number,
            case_references,
            created_time,
            updated_time
        ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        // Row 1
        jdbcTemplate.update(sql, masterId, correlationId1, hearingId, "ACC-001", "Case A", LocalDateTime.now(), LocalDateTime.now());
        // Row 2
        jdbcTemplate.update(sql, masterId, correlationId2, hearingId, "ACC-002", "Case B", LocalDateTime.now(), LocalDateTime.now());

        // Act
        List<DefendantGOBAccount> results = repository.findAllById_MasterDefendantIdAndHearingId(masterId, hearingId);

        // Assert
        assertThat(results).hasSize(2);

        // verify the data belongs to the requested IDs
        assertThat(results).allSatisfy(account -> {
            assertThat(account.getMasterDefendantId()).isEqualTo(masterId);
            assertThat(account.getHearingId()).isEqualTo(hearingId);
        });

        // Verify individual correlationId's exist in the collection
        assertThat(results).extracting(DefendantGOBAccount::getAccountCorrelationId)
            .containsExactlyInAnyOrder(correlationId1, correlationId2);
        // Verify individual account numbers exist in the collection
        assertThat(results).extracting(DefendantGOBAccount::getAccountNumber)
            .containsExactlyInAnyOrder("ACC-001", "ACC-002");
    }

    @Test
    @DisplayName("findAllByMasterIdAndHearingId should return an empty list when no matching records exist")
    void shouldReturnEmptyList_whenNoRecordsMatchCriteria() {

        // Arrange
        UUID masterId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();

        List<DefendantGOBAccount> results = repository.findAllById_MasterDefendantIdAndHearingId(masterId, hearingId);

        assertThat(results).isEmpty();
    }

}