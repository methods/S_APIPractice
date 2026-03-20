package com.example.HearingsDemo.hearing;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record HearingResponseDTO (
    @Schema(description = "Unique identifier for the hearing", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID hearingId,

    @Schema(description = "The scheduled start date and time of the hearing", example = "2024-12-01T10:30:00")
    LocalDateTime startDate,

    @Schema(description = "The name of the court centre where the hearing is held", example = "Birmingham Crown Court")
    String courtCentreName,

    @Schema(description = "The name of the presiding judge", example = "HHJ Taylor")
    String judgeName,

    @Schema(description = "List of all individuals attending the hearing")
    List<AttendeeDTO> attendees // Ids + Names of all people in those rows
) {}