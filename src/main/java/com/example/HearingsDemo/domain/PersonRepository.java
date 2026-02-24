package com.example.HearingsDemo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

// 1. @Repository: Tells Spring "this bean talks to the DB"
// 2. extends JpaRepository: inherits all methods from this class
// 3. <Person, PersonId>: The Generics:
//          - First: the Entity we are managing.
//          - Second: the Data Type of the Primary Key 
//          (Because we have a Composite Key, we MUST put 'PersonId' here, not 'UUID')

@Repository
public interface PersonRepository extends JpaRepository<Person, PersonId> {

    // Find everyone in a specific hearing (Partial Key Search Part A)
    List<Person> findByHearingId(UUID hearingId);

    // Find every hearing for a specific person (Partial Key Search Part B)
    List<Person> findById(UUID id);
    
}
