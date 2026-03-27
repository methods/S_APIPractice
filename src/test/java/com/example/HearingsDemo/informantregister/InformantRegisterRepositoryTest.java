package com.example.HearingsDemo.informantregister;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * GET /api/v1/informant-reporting/{id} NOTE: Decoupled the API name (informant-reporting) from the Table name
 * (informant_register) for security and loose coupling
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InformantRegisterRepositoryTest {

    @Autowired
    private InformantRegisterRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // ===============================
    // FindByID single lookup
    // ===============================

    @Test
    @DisplayName("Should find InformantRegister by ID with all fields populated")
    void shouldFindInformantRegisterById() {

        // Arrange // Define your test data variables
        UUID id = UUID.randomUUID();
        UUID prosecutionAuthorityId = UUID.randomUUID();
        UUID fileId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        String payload = "{\"data\": \"sample payload\"}";
        String status = "PROCESSED";

        String sql = """
            INSERT INTO informant_register (
                id,
                prosecution_authority_id,
                prosecution_authority_code,
                prosecution_authority_ou_code,
                register_date,
                file_id,
                payload,
                status,
                processed_on,
                hearing_id,
                register_time,
                generated_time,
                generated_date
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
            id,                               // id (UUID)
            prosecutionAuthorityId,           // prosecution_authority_id (UUID)
            "PA-CODE-001",                    // prosecution_authority_code (TEXT)
            "OU-CODE-99",                     // prosecution_authority_ou_code (TEXT)
            today,                            // register_date (date)
            fileId,                           // file_id (UUID)
            payload,                          // payload (TEXT)
            status,                           // status (TEXT)
            now,                              // processed_on (datetime)
            hearingId,                        // hearing_id (UUID)
            now,                              // register_time (datetime)
            now,                              // generated_time (datetime)
            today                             // generated_date (date)
        );

        // Act
        Optional<InformantRegister> result = repository.findById(id);

        // Assert
        assertThat(result).isPresent().hasValueSatisfying(register -> {
            assertThat(register.getId()).isEqualTo(id);
            assertThat(register.getProsecutionAuthorityId()).isEqualTo(prosecutionAuthorityId);
            assertThat(register.getProsecutionAuthorityCode()).isEqualTo("PA-CODE-001");
            assertThat(register.getProsecutionAuthorityOuCode()).isEqualTo("OU-CODE-99");

            // Asserting Dates (LocalDate)
            assertThat(register.getRegisterDate()).isEqualTo(today);
            assertThat(register.getGeneratedDate()).isEqualTo(today);

            // Asserting Data/Strings
            assertThat(register.getFileId()).isEqualTo(fileId);
            assertThat(register.getPayload()).isEqualTo(payload);
            assertThat(register.getStatus()).isEqualTo("PROCESSED");

            // Asserting Timestamps (LocalDateTime)
            assertThat(register.getProcessedOn()).isEqualTo(now);
            assertThat(register.getHearingId()).isEqualTo(hearingId);
            assertThat(register.getRegisterTime()).isEqualTo(now);
            assertThat(register.getGeneratedTime()).isEqualTo(now);
        });
    }

    @Test
    @DisplayName("Should handle NULL values for optional fields in InformantRegister")
    void shouldFindInformantRegister_whenOptionalFieldsAreNull() {
        // Arrange: Only provide the MANDATORY fields (nullable=false in Liquibase)
        UUID id = UUID.randomUUID();
        UUID prosecutionAuthorityId = UUID.randomUUID();
        LocalDate registerDate = LocalDate.now();
        String payload = "{\"minimal\": true}";

        // All other fields passed as NULL in the SQL
        String sql = """
        INSERT INTO informant_register (
            id, prosecution_authority_id, register_date, payload,
            prosecution_authority_code, status, file_id, hearing_id
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
            id,                     // id (NOT NULL)
            prosecutionAuthorityId, // prosecution_authority_id (NOT NULL)
            registerDate,           // register_date (NOT NULL)
            payload,                // payload (NOT NULL)
            null,                   // prosecution_authority_code (NULLABLE)
            null,                   // status (NULLABLE)
            null,                   // file_id (NULLABLE)
            null                    // hearing_id (NULLABLE)
        );

        // Act
        Optional<InformantRegister> result = repository.findById(id);

        // Assert
        assertThat(result).isPresent().hasValueSatisfying(register -> {
            // 1. Check Mandatory fields still work
            assertThat(register.getId()).isEqualTo(id);
            assertThat(register.getPayload()).isEqualTo(payload);

            // 2. Check Optional fields are safely NULL (No NullPointerException!)
            assertThat(register.getProsecutionAuthorityCode()).isNull();
            assertThat(register.getStatus()).isNull();
            assertThat(register.getFileId()).isNull();
            assertThat(register.getHearingId()).isNull();
            assertThat(register.getProcessedOn()).isNull();
        });
    }

    @Test
    @DisplayName("findById should return an empty Optional when the UUID is not in the database")
    void shouldReturnEmptyOptional_whenIdDoesNotExist() {
        // Act
        Optional<InformantRegister> result = repository.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
        assertThat(result).isNotPresent();

    }
}