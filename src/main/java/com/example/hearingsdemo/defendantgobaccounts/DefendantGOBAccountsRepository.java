package com.example.hearingsdemo.defendantgobaccounts;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DefendantGOBAccountsRepository extends Repository<DefendantGOBAccount, DefendantGOBAccountId> {

    /**
     * Retrieves one specific, unique account record.

     * @param id The composite key to search with.
     * @return A specific, unique account record.
     */
    Optional<DefendantGOBAccount> findById(DefendantGOBAccountId id);


    /**
     * Finds all accounts linked to a specific defendant within a specific hearing.
     * @param masterDefendantId  The unique identifier for the defendant
     * @param hearingId The unique identifier for the hearing
     * @return A list of matching DefendantGOBAccount records
     */
    List<DefendantGOBAccount> findAllById_MasterDefendantIdAndHearingId(UUID masterDefendantId, UUID hearingId);

}

