package com.example.HearingsDemo.defendantgobaccounts;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record DefendantGOBAccountDTO(
    @Schema(description = "Unique identifier for the master defendant", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID masterDefendantId,

    @Schema(description = "Correlation ID for the account request", example = "721e8400-e29b-41d4-a716-446655441111")
    UUID accountCorrelationId,

    @Schema(description = "Unique identifier for the associated hearing", example = "a12e8400-e29b-41d4-a716-446655442222")
    UUID hearingId,

    @Schema(description = "The specific account number (Optional)", example = "ACT-77652")
    String accountNumber,

    @Schema(description = "The timestamp when the account was requested (Optional)", example = "2026-03-31T10:15:30")
    LocalDateTime accountRequestTime,

    @Schema(description = "References for the specific case", example = "T20230001, T20230002")
    String caseReferences

) {
}