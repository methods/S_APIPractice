package com.example.HearingsDemo.informantregister;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface InformantRegisterRepository extends Repository<InformantRegister, UUID> {

    /**
     * Retrieves the informant report associated with a specific ID.
     * GET /api/v1/informant-reporting/{id}
     * @param id the unique identifier
     * @return an Optional containing the report if found, otherwise empty
     */
    Optional<InformantRegister> findById(UUID id);
}