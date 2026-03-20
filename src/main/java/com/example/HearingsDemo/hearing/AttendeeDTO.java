package com.example.HearingsDemo.hearing;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record AttendeeDTO(
    @Schema(description = "Unique identifier for the person", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID personId,

    @Schema(description = "The person's given name", example = "John")
    String firstName,

    @Schema(description = "The person's family name", example = "Doe")
    String lastName
) {}
