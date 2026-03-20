package com.example.HearingsDemo.hearing;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Hearing Validation", description = "Endpoints for verifying Hearing data integrity")
@RestController
@RequestMapping("/api/v1")
public class HearingController {

    private final HearingService hearingService;

    public HearingController(HearingService hearingService) {
        this.hearingService = hearingService;
    }

    @Operation(summary = "Fetch a Hearing by Hearing ID (UUID)", description = "Returns a nested DTO including all " +
        "attendees found for the hearing.")
    @GetMapping("/hearings/{hearingId}")
    public ResponseEntity<HearingResponseDTO> getHearingById(
        @PathVariable UUID hearingId // <- Name must match the URL
    ) {

        HearingResponseDTO response = hearingService.getHearingById(hearingId);
        return ResponseEntity.ok(response);
    }
}