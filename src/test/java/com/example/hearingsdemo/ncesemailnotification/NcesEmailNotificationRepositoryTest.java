package com.example.hearingsdemo.ncesemailnotification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * GET /api/v1/nces-notifications/{id}
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NcesEmailNotificationRepositoryTest {

    @Autowired
    private NcesEmailNotificationRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ===============================
    // FindByID single lookup
    // ===============================

    @Test
    @DisplayName("Should find NcesEmailNotification by ID with all fields populated")
    void shouldFindTheEmailById() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        UUID masterDefendantId = UUID.randomUUID();
        String sendTo = "Example_person";
        String subject = "Test Subject note";

        String sql = """
        INSERT INTO nces_email_notification (
            id,
            material_id,
            notification_id,
            master_defendant_id,
            send_to,
            subject
        ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
            id,
            materialId,
            notificationId,
            masterDefendantId,
            sendTo,
            subject
        );

        // Act
        Optional<NcesEmailNotification> result = repository.findById(id);

        // Assert
        assertThat(result).isPresent().hasValueSatisfying(notification -> {
            // Identifiers
            assertThat(notification.getId()).isEqualTo(id);
            assertThat(notification.getMaterialId()).isEqualTo(materialId);
            assertThat(notification.getNotificationId()).isEqualTo(notificationId);
            assertThat(notification.getMasterDefendantId()).isEqualTo(masterDefendantId);

            // Strings / Data
            assertThat(notification.getSendTo()).isEqualTo("Example_person");
            assertThat(notification.getSubject()).isEqualTo("Test Subject note");
        });
    }

    @Test
    @DisplayName("Should handle NULL values for optional fields in NcesEmailNotification")
    void shouldFindEmailNotification_whenOptionalFieldsAreNull() {

        // Arrange
        UUID id = UUID.randomUUID();

        String sql = """
        INSERT INTO nces_email_notification (
            id,
            material_id,
            notification_id,
            master_defendant_id,
            send_to,
            subject
        ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
            id,
            null,
            null,
            null,
            null,
            null
        );

        // Act
        Optional<NcesEmailNotification> result = repository.findById(id);

        // Assert
        assertThat(result).isPresent().hasValueSatisfying(notification -> {
            // Identifiers
            assertThat(notification.getId()).isEqualTo(id);

            // Optional fields are safely NULL
            assertThat(notification.getMaterialId()).isNull();
            assertThat(notification.getNotificationId()).isNull();
            assertThat(notification.getMasterDefendantId()).isNull();
            assertThat(notification.getSendTo()).isNull();
            assertThat(notification.getSubject()).isNull();
        });

    }

    @Test
    @DisplayName("findById should return an empty Optional when the UUID is not in the database")
    void shouldReturnEmptyOptional_whenIdDoesNotExist() {
        // Act
        Optional<NcesEmailNotification> result = repository.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

}