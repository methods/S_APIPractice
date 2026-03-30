package com.example.HearingsDemo.ncesemailnotification;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record NcesEmailNotificationDTO(
    @Schema(description = "Primary key identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Schema(description = "Associated material identifier", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    UUID materialId,

    @Schema(description = "Associated notification identifier", example = "7c9e6679-7425-40de-944b-e07fc1f90ae7")
    UUID notificationId,

    @Schema(description = "Associated master defendant identifier", example = "3d2f1a6b-c7e5-4a21-9d82-0b1a2c3d4e5f")
    UUID masterDefendantId,

    @Schema(description = "The recipient of the email notification", example = "Example_person")
    String sendTo,

    @Schema(description = "The subject line of the email notification", example = "Test Subject note")
    String subject
) {
}