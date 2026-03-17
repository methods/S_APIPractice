package com.example.HearingsDemo.hearing;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class HearingService {

    private final HearingRepository hearingRepository;

    public HearingService(HearingRepository hearingRepository) {
        this.hearingRepository = hearingRepository;
    }

    // --- Single lookup ---
    public HearingResponseDTO getHearingById(UUID hearingId) {

        // Fetch rows
        List<Hearing> rows = hearingRepository.findAllById_HearingUuid(hearingId);

        // Handle Not Found
        if (rows.isEmpty()) {
            throw new ResourceNotFoundException("Hearing not found with ID: " + hearingId);
        }

        return mapToDTO(rows);

    }

    // --- Helper Mapper: Takes a List, returns a Single DTO
    private HearingResponseDTO mapToDTO(List<Hearing> rows) {

        // Get shared details: Deduplication
        Hearing firstRow = rows.get(0);

        // Gather attendee IDs
        List<UUID> attendeeIds = rows.stream()
            .map(h -> h.getId().getPersonUuid())
            .toList();

        return new HearingResponseDTO(
            firstRow.getId().getHearingUuid(),
            firstRow.getStartDate(),
            firstRow.getCourtCentreName(),
            firstRow.getJudgeName(),
            attendeeIds
        );
    }


}