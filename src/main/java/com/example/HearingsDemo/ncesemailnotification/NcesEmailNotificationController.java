package com.example.HearingsDemo.ncesemailnotification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Tag(name ="...", description = "...")
@RestController
@RequestMapping("/api/v1/nces-notifications")
public class NcesEmailNotificationController {

    private final NcesEmailNotificationService service;

    public NcesEmailNotificationController(NcesEmailNotificationService service) {
        this.service = service;
    }

    @Operation(
        summary = "...",
        description = "..."
    )
    @GetMapping("/{id}")
    public ResponseEntity<NcesEmailNotificationDTO> getNotificationById(
        @PathVariable UUID id
    ) {
        // Call the service or throw Exception
        NcesEmailNotificationDTO resultDto = service.getNotificationById(id);

        return ResponseEntity.ok(resultDto);
    }
}