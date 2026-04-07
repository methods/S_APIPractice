package com.example.hearingsdemo.defendantTrackingStatus;

import com.example.hearingsdemo.exception.ResourceNotFoundException;
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
class DefendantTrackingStatusServiceTest {

    @Mock
    private DefendantTrackingStatusRepository mockRepository;

    @InjectMocks
    private DefendantTrackingStatusService service;

    // ======================================================================
    // A simple utility class for creating test entity objects
    // ======================================================================

    private DefendantTrackingStatus createEntity(UUID offenceId, UUID defendantId) {
        return new DefendantTrackingStatus(
            offenceId,
            defendantId,           // defendantId
            false,                 // emStatus
            LocalDateTime.now(),   // emLastModifiedTime
            false,                 // woaStatus
            LocalDateTime.now()    // woaLastModifiedTime
        );
    }

    @Test
    @DisplayName("getDefendantTrackingStatusByOffenceId should return tracking_status when offenceId found")
    void shouldReturnTrackingStatus_whenOffenceIdFound() {

        // Arrange
        UUID offenceId = UUID.randomUUID();
        UUID defendantId = UUID.randomUUID();

        // Create fake entities
        DefendantTrackingStatus entity = createEntity(offenceId, defendantId);

        when(mockRepository.findById(offenceId)).thenReturn(Optional.of(entity));

        // Act
        DefendantTrackingStatusDTO results = service.getDefendantTrackingStatusByOffenceId(offenceId);

        // Assert
        assertThat(results).isNotNull();
        assertThat(results.offenceId()).isEqualTo(offenceId);
        assertThat(results.defendantId()).isEqualTo(defendantId);
        assertThat(results.emStatus()).isFalse();
        assertThat(results.emLastModifiedTime()).isNotNull();
        assertThat(results.woaStatus()).isFalse();
        assertThat(results.woaLastModifiedTime()).isNotNull();

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    void shouldThrowResourceNotFoundException_whenOffenceIdDoesNotExist() {
        // Arrange
        UUID offenceId = UUID.randomUUID();
        when(mockRepository.findById(offenceId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getDefendantTrackingStatusByOffenceId(offenceId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Tracking status not found");

    }
}