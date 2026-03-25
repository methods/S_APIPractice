package com.example.HearingsDemo.hearingResultedDocument;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HearingDocumentRepositoryTest {

    @Autowired
    private HearingResultedDocumentRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ===========================================================
    // Partial Key lookup
    // ===========================================================

    @Test
    void shouldFindAllDocumentsForGivenHearingUuidAndIgnoreOthers() {

        // ARRANGE - Seed data using RAW SQL
        UUID hearingUuid = UUID.randomUUID();

        // Write a real SQL INSERT statement
        String sql = "INSERT INTO hearing_resulted_document (hearing_id, hearing_day, start_date, payload) VALUES (?, ?, ?, ?)";

        // Use JdbcTemplate to execute it. This bypasses Hibernate's "insertable=false" rule!
        jdbcTemplate.update(sql, hearingUuid, java.sql.Date.valueOf("2023-10-31"), java.sql.Date.valueOf("2023-10-31"),
            "{\"result\": \"Success\"}");

        // ACT
        List<HearingResultedDocument> results = repository.findByHearingResultedDocumentId_HearingUuid(hearingUuid);

        // ASSERT
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPayload()).isEqualTo("{\"result\": \"Success\"}");
    }
}