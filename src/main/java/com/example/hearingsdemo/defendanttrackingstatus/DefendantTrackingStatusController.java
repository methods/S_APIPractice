package com.example.hearingsdemo.defendanttrackingstatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Defendant Tracking Status Viewstore", description = "Query Endpoints for monitoring defendant status")
@RestController
@RequestMapping("/api/v1/offences")
public class DefendantTrackingStatusController {

    private final DefendantTrackingStatusService service;

    public DefendantTrackingStatusController(DefendantTrackingStatusService service) {
        this.service = service;
    }

    @Operation(summary = "Get tracking status", description = "Retrieves status by offence ID")
    @GetMapping("/{offenceId}/tracking-status")
    public ResponseEntity<DefendantTrackingStatusDTO> getStatusByOffenceId(
        @PathVariable UUID offenceId
    ) {
        // 1. Call the service or throw Exception
        DefendantTrackingStatusDTO statusDto = service.getDefendantTrackingStatusByOffenceId(offenceId);

        return ResponseEntity.ok(statusDto);
    }
}