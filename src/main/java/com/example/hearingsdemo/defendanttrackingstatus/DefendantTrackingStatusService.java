package com.example.hearingsdemo.defendanttrackingstatus;

import com.example.hearingsdemo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class DefendantTrackingStatusService {

    private final DefendantTrackingStatusRepository repository;

    public DefendantTrackingStatusService(DefendantTrackingStatusRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves the tracking status associated with a specific offence.
     * Returns the DTO directly or throws ResourceNotFoundException.
     */
    public DefendantTrackingStatusDTO getDefendantTrackingStatusByOffenceId(UUID offenceId) {

        // 1. Find the entity OR throw the exception immediately
        DefendantTrackingStatus statusEntity = repository.findById(offenceId)
            .orElseThrow(() -> new ResourceNotFoundException("Tracking status not found for offence: " + offenceId));

        // Map the found entity to the DTO and return it
        return mapToDTO(statusEntity);
    }

    // Mapping Helper method
    private DefendantTrackingStatusDTO mapToDTO(DefendantTrackingStatus statusEntity) {
        return new DefendantTrackingStatusDTO(
            statusEntity.getOffenceId(),
            statusEntity.getDefendantId(),
            statusEntity.getEmStatus(),
            statusEntity.getEmLastModifiedTime(),
            statusEntity.getWoaStatus(),
            statusEntity.getWoaLastModifiedTime()
        );
    }

}