package com.example.HearingsDemo.web;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    public Optional<PersonResponseDTO> getPersonById(UUID hearingId, UUID personId) {
        // TODO: Implement Entity -> DTO mapping with Composite Key lookup
        return Optional.empty(); // stubbed to compile for now
    }

}