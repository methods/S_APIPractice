package com.example.HearingsDemo.ncesemailnotification;

import com.example.HearingsDemo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class NcesEmailNotificationService {

    private final NcesEmailNotificationRepository repository;

    public  NcesEmailNotificationService(NcesEmailNotificationRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves the notification associated with a specific ID.
     * Returns the DTO directly or throws ResourceNotFoundException.
     */
    public NcesEmailNotificationDTO getNotificationById(UUID id){

        NcesEmailNotification resultEntity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found for id: " + id));

        return mapToDTO(resultEntity);
    }

    /**
     * Maps the Immutable Entity (Database Model) to a Record DTO (API Model).
     */
    private NcesEmailNotificationDTO mapToDTO(NcesEmailNotification entity) {
        return new NcesEmailNotificationDTO(
            entity.getId(),
            entity.getMaterialId(),
            entity.getNotificationId(),
            entity.getMasterDefendantId(),
            entity.getSendTo(),
            entity.getSubject()
        );
    }
}