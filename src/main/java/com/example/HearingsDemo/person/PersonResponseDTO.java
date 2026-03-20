package com.example.HearingsDemo.person;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record PersonResponseDTO(
    @Schema(description = "The unique identifier for the person", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID personId,

    @Schema(description = "The ID of the hearing this person record is associated with", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID hearingId,

    @Schema(description = "The person's first name", example = "Jane")
    String firstName,

    @Schema(description = "The person's last name", example = "Smith")
    String lastName
) {

}
