package com.example.HearingsDemo.hearingResultedDocument;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "HearingResultsDocument Validation", description = "Endpoints for verifying HearingResultsDocument data integrity")
@RestController
@RequestMapping("/api/v1")
public class HearingResultedDocumentController {

    private final HearingResultedDocumentService service;

    public HearingResultedDocumentController(HearingResultedDocumentService service) {
        this.service = service;
    }

    @GetMapping("/hearings/{hearingId}")
    public ResponseEntity<HearingDocumentCollectionResponseDTO> getDocuments(
        @PathVariable UUID hearingId
    ){

        List<HearingDocumentResponseDTO> docs = service.getAllDocumentsByHearingId(hearingId);

        return ResponseEntity.ok(new HearingDocumentCollectionResponseDTO(docs, docs.size()));
    }

}