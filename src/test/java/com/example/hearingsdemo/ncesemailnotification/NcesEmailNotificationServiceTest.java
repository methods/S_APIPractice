package com.example.hearingsdemo.ncesemailnotification;


import com.example.hearingsdemo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NcesEmailNotificationServiceTest {

    @Mock
    private NcesEmailNotificationRepository mockRepository;

    @InjectMocks
    private NcesEmailNotificationService service;

    // ======================================================================
    // A simple utility method for creating test NcesEmailNotification objects
    // ======================================================================
    private NcesEmailNotification createEntity(UUID id) {
        return new NcesEmailNotification(
            id,
            UUID.randomUUID(),           // material_id
            UUID.randomUUID(),           // notification_id
            UUID.randomUUID(),           // master_defendant_id
            "Test send to",              // send_to
            "Test Subject"               // subject
        );
    }

    @Test
    @DisplayName("getNotificationById: Should return a fully mapped DTO when the record exists in the repository")
    void shouldReturnDto_whenRecordExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        // Create a fake entity to pass as mock
        NcesEmailNotification entity = createEntity(id);

        when(mockRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        NcesEmailNotificationDTO result = service.getNotificationById(id);

        // Assert
        assertThat(result).isNotNull();

        // Identifiers
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.materialId()).isEqualTo(entity.getMaterialId());
        assertThat(result.notificationId()).isEqualTo(entity.getNotificationId());
        assertThat(result.masterDefendantId()).isEqualTo(entity.getMasterDefendantId());

        // String
        assertThat(result.sendTo()).isEqualTo(entity.getSendTo());
        assertThat(result.subject()).isEqualTo(entity.getSubject());

    }

    @Test
    @DisplayName("getNotificationById: Should throw ResourceNotFoundException when the requested ID is missing")
    void shouldThrowResourceNotFoundException_whenRecordDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(mockRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getNotificationById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Notification not found for id: " + id);



    }
}