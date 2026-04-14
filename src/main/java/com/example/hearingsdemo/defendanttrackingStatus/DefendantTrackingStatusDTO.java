package com.example.hearingsdemo.defendanttrackingStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record DefendantTrackingStatusDTO(
    @Schema(description = "Unique identifier for the offence", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID offenceId,

    @Schema(description = "Unique identifier for the defendant", example = "721e8400-e29b-41d4-a716-446655441111")
    UUID defendantId,

    @Schema(description = "Electronic Monitoring status", example = "true")
    Boolean emStatus,

    @Schema(description = "Last time the EM status was updated", example = "2023-10-27T10:15:30")
    LocalDateTime emLastModifiedTime,

    @Schema(description = "Warrant of Arrest status", example = "false")
    Boolean woaStatus,

    @Schema(description = "Last time the WOA status was updated", example = "2023-10-27T11:30:00")
    LocalDateTime woaLastModifiedTime
) {
}