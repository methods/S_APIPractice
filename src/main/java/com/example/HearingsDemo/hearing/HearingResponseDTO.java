package com.example.HearingsDemo.hearing;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record HearingResponseDTO (
    UUID hearingId,
    LocalDateTime startDate,
    String courtCentreName, // Note: Liquibase called it court_centre_name
    String judgeName,
    List<AttendeeDTO> attendees // Ids of all people in those rows
) {}