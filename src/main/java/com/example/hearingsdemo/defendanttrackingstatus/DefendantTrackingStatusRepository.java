package com.example.hearingsdemo.defendanttrackingstatus;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface DefendantTrackingStatusRepository extends Repository<DefendantTrackingStatus, UUID> {

    /**
     * Retrieves the tracking status associated with a specific offence.
     * Maps to: GET /api/v1/offences/{offenceId}/tracking-status
     *
     * @param offenceId the unique identifier of the offence
     * @return an Optional containing the status if found, otherwise empty
     */
    Optional<DefendantTrackingStatus> findById(UUID offenceId);
}
