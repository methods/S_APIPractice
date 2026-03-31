package com.example.HearingsDemo.defendantgobaccounts;

import com.example.HearingsDemo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class DefendantGOBAccountsService {

    private final DefendantGOBAccountsRepository repository;

    public DefendantGOBAccountsService(DefendantGOBAccountsRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves account record for given IDs and maps to a DTO.
     * @param masterDefendantId The unique ID of the defendant
     * @param accountCorrelationId The correlation ID of the account
     * @return DefendantGOBAccountDTO containing the mapped data
     * @throws ResourceNotFoundException if the account does not exist
     */
    public DefendantGOBAccountDTO getAccountByIds(UUID masterDefendantId, UUID accountCorrelationId) {

        DefendantGOBAccountId compositeKey = new DefendantGOBAccountId(masterDefendantId, accountCorrelationId);

        DefendantGOBAccount entity = repository.findById(compositeKey)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found for the given IDs"));

        return mapToDTO(entity);
    }

    // Mapping Helper method
    private DefendantGOBAccountDTO mapToDTO(DefendantGOBAccount entity) {
        return new DefendantGOBAccountDTO(
            // Pulling fields out of the Composite Key (@EmbeddedId)
            entity.getMasterDefendantId(),
            entity.getAccountCorrelationId(),

            // Pulling fields from the main Entity
            entity.getHearingId(),
            entity.getAccountNumber(),
            entity.getAccountRequestTime(),
            entity.getCaseReferences()
        );
    }
}