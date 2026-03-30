package com.example.HearingsDemo.ncesemailnotification;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface NcesEmailNotificationRepository extends Repository<NcesEmailNotification, UUID> {
    /**
     * Retrieves the NCES email notification associated with a specific ID.
     * GET /api/v1/nces-notifications/{id}
     * @param id the unique identifier
     * @return an Optional containing the report if found, otherwise empty
     */
    Optional<NcesEmailNotification> findById(UUID id);
}