package com.example.HearingsDemo.defendantgobaccounts;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface DefendantGOBAccountsRepository extends Repository<DefendantGOBAccount, DefendantGOBAccountId> {

    /**
     * Retrieves one specific, unique account record.

     * @param id The composite key to search with.
     * @return A specific, unique account record.
     */
    Optional<DefendantGOBAccount> findById(DefendantGOBAccountId id);
}

