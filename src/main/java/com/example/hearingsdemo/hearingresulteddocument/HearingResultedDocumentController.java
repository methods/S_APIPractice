package com.example.hearingsdemo.hearingresulteddocument;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Hearing Results Viewstore", description = "Query endpoints for retrieving processed hearing result documents.")
@RestController
@RequestMapping("/api/v1/hearings")
public class HearingResultedDocumentController {

    private final HearingResultedDocumentService service;

    public HearingResultedDocumentController(HearingResultedDocumentService service) {
        this.service = service;
    }

    @Operation(
        summary = "Get all result documents for a hearing",
        description = "Retrieves a collection of all result payloads associated with a specific hearing UUID, organized by hearing day."
    )
    @GetMapping("/{hearingId}")
    public ResponseEntity<HearingDocumentCollectionResponseDTO> getDocuments(
        @PathVariable UUID hearingId
    ){

        List<HearingDocumentResponseDTO> docs = service.getAllDocumentsByHearingId(hearingId);

        return ResponseEntity.ok(new HearingDocumentCollectionResponseDTO(docs, docs.size()));
    }

}