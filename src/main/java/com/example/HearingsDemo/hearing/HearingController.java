package com.example.HearingsDemo.hearing;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class HearingController {

    private final HearingService hearingService;

    public HearingController(HearingService hearingService) {
        this.hearingService = hearingService;
    }

    @GetMapping("/hearings/{hearingId}")
    public ResponseEntity<HearingResponseDTO> getHearingById(
        @PathVariable UUID hearingId // <- Name must match the URL
    ) {

        HearingResponseDTO response = hearingService.getHearingById(hearingId);
        return ResponseEntity.ok(response);
    }
}