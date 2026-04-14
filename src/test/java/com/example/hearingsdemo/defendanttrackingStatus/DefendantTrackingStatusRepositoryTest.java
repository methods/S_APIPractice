package com.example.hearingsdemo.defendanttrackingStatus;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DefendantTrackingStatusRepositoryTest {

    @Autowired
    private DefendantTrackingStatusRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ===============================
    // FindByID single lookup
    // ===============================

    @Test
    void shouldFindTrackingStatusByOffenceId() {

        // Arrange
        UUID offenceId = UUID.randomUUID();
        UUID defendantId = UUID.randomUUID();
        // Create the timestamps
        LocalDateTime now = LocalDateTime.now();

        String sql = "INSERT INTO defendant_tracking_status (offence_id, defendant_id, em_status, " +
            "em_last_modified_time, " +
            "woa_status, woa_last_modified_time) VALUES (?, ?, ?, ? , ? ,?)";

        jdbcTemplate.update(sql,
            offenceId,
            defendantId,
            true,   // em_status (Boolean)
            now,    // em_last_modified_time (datetime)
            false,  // woa_status (Boolean)
            now     // woa_last_modified_time (datetime)
            );

        // Act
        Optional<DefendantTrackingStatus> results = repository.findById(offenceId);

        // ASSERT
        assertThat(results).isPresent();
        assertThat(results.get().getOffenceId()).isEqualTo(offenceId);
        assertThat(results.get().getDefendantId()).isEqualTo(defendantId);

    }

    @Test
    void shouldFindTrackingStatus_whenOptionalFieldsAreNull() {
        // Arrange
        UUID offenceId = UUID.randomUUID();
        UUID defendantId = UUID.randomUUID();

        String sql = "INSERT INTO defendant_tracking_status (offence_id, defendant_id, em_status, " +
            "em_last_modified_time, " +
            "woa_status, woa_last_modified_time) VALUES (?, ?, ?, ? , ? ,?)";

        jdbcTemplate.update(sql,
            offenceId,
            defendantId,
            null,    // em_status (Boolean)
            null,    // em_last_modified_time (datetime)
            null,    // woa_status (Boolean)
            null     // woa_last_modified_time (datetime)
        );

        // Act
        Optional<DefendantTrackingStatus> results = repository.findById(offenceId);
        // ASSERT
        assertThat(results).isPresent();
        DefendantTrackingStatus status = results.get(); // extract once for readability
        assertThat(status.getEmStatus()).isNull();
        assertThat(status.getEmLastModifiedTime()).isNull();
        assertThat(status.getWoaStatus()).isNull();
        assertThat(status.getWoaLastModifiedTime()).isNull();

    }

}