package com.example.HearingsDemo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

// 1. @Repository: Marks this interface as a persistence componenet managed by Spring - Tells Spring "this bean talks to the DB"
// 2. extends JpaRepository: inherits CRUD and query methods from this class (Spring Data JPA)
// 3. <Person, PersonId>: Generic parameters:
//      - Entity type
//      - Primary key type (composite key via PersonId)
//        (Because we have a Composite Key, we MUST put 'PersonId' here, not 'UUID')

@Repository
public interface PersonRepository extends JpaRepository<Person, PersonId> {

    // Find everyone in a specific hearing (Partial Key Search Part A)
    List<Person> findByHearingId(UUID hearingId);

    // Find every hearing for a specific person (Partial Key Search Part B)
    List<Person> findById(UUID id);
    
}
