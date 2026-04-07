package com.example.hearingsdemo.informantregister;

import com.example.hearingsdemo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class InformantRegisterService {

    private final InformantRegisterRepository repository;

    public InformantRegisterService(InformantRegisterRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves the informant result associated with a specific ID.
     * Returns the DTO directly or throws ResourceNotFoundException.
     */
    public InformantRegisterDTO getInformantResultById(UUID id) {

        // 1. Find the entity OR throw the exception immediately
        InformantRegister resultEntity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Informant result not found for id: " + id));

        return mapToDTO(resultEntity);
    }

    /**
     * Maps the Immutable Entity (Database Model) to a Record DTO (API Model).
     */
    private InformantRegisterDTO mapToDTO(InformantRegister entity) {
        return new InformantRegisterDTO(
            entity.getId(),
            entity.getProsecutionAuthorityId(),
            entity.getProsecutionAuthorityCode(),
            entity.getProsecutionAuthorityOuCode(),
            entity.getRegisterDate(),
            entity.getFileId(),
            entity.getPayload(),
            entity.getStatus(),
            entity.getProcessedOn(),
            entity.getHearingId(),
            entity.getRegisterTime(),
            entity.getGeneratedTime(),
            entity.getGeneratedDate()
        );
    }
}
