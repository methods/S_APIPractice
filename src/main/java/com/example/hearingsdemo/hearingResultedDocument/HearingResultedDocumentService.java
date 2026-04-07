package com.example.hearingsdemo.hearingResultedDocument;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class HearingResultedDocumentService {

    private final HearingResultedDocumentRepository repository;

    public HearingResultedDocumentService( HearingResultedDocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all hearing documents for a given hearing UUID and maps them to DTOs.
     *
     * @param hearingUuid The UUID of the hearing.
     * @return A list of HearingDocumentResponse DTOs. Returns an empty list if no documents are found.
     *
     */
    public List<HearingDocumentResponseDTO> getAllDocumentsByHearingId(UUID hearingUuid) {

        // 1. Call the repository to get the database entities
        List<HearingResultedDocument> documentsEntities =
            repository.findAllByHearingResultedDocumentId_HearingUuid(hearingUuid);

        // 2. Use a Java Stream to map each entity to a DTO
        return documentsEntities.stream()
            .map(this::mapToDTO)
            .toList();

    }

    // Private Mapping Helper method
    private HearingDocumentResponseDTO mapToDTO(HearingResultedDocument documentEntity) {
        return new HearingDocumentResponseDTO(
            documentEntity.getHearingResultedDocumentId().getHearingDay(),
            documentEntity.getPayload()
        );
    }
}