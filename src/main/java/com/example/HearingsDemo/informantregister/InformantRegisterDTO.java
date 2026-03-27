package com.example.HearingsDemo.informantregister;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record InformantRegisterDTO(
    @Schema(description = "Primary key identifier", example = "a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6")
    UUID id,

    @Schema(description = "The ID of the prosecution authority", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID prosecutionAuthorityId,

    @Schema(description = "Code for the prosecution authority", example = "PA-001")
    String prosecutionAuthorityCode,

    @Schema(description = "OU code for the prosecution authority", example = "OU-99")
    String prosecutionAuthorityOuCode,

    @Schema(description = "Date the record was registered", example = "2026-03-27")
    LocalDate registerDate,

    @Schema(description = "Associated file identifier", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    UUID fileId,

    @Schema(description = "The raw payload data", example = "{ \"key\": \"value\" }")
    String payload,

    @Schema(description = "Current processing status", example = "PROCESSED")
    String status,

    @Schema(description = "Timestamp when the record was processed", example = "2026-03-27T10:30:00")
    LocalDateTime processedOn,

    @Schema(description = "Associated hearing identifier", example = "b8f421a1-9876-4c32-1234-987654321abc")
    UUID hearingId,

    @Schema(description = "Specific time of registration", example = "2026-03-27T09:00:00")
    LocalDateTime registerTime,

    @Schema(description = "Timestamp when the record was generated", example = "2026-03-27T08:45:00")
    LocalDateTime generatedTime,

    @Schema(description = "Date the record was generated", example = "2026-03-27")
    LocalDate generatedDate
) {
}