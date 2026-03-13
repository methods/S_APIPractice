package com.example.HearingsDemo.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface PersonRepository extends JpaRepository<Person, PersonId> {

    // Find everyone in a specific hearing (Partial Key Search Part A)
    List<Person> findAllById_HearingId(UUID hearingId);

    // Find every hearing for a specific person (Partial Key Search Part B)
    List<Person> findAllById_PersonUuid(UUID id);
    
}
