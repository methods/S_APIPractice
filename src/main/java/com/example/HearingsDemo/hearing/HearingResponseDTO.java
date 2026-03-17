package com.example.HearingsDemo.hearing;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record HearingResponseDTO (
    UUID hearingUuid,
    LocalDateTime startDate,
    String courtRoom, // Note: Liquidbase called it court_centre_name
    String judgeName,
    List<UUID> attendeeIds // Ids of all people in those rows
) {}