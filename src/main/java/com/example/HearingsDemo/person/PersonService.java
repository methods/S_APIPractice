package com.example.HearingsDemo.person;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.stream;


@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // --- Single lookup ---
    public Optional<PersonResponseDTO> getPersonById(UUID personId, UUID hearingId) {
        // 1. Instantiate comp key
        PersonId compositeKey = new PersonId(personId, hearingId);

        // 2. Call repo to find the data
        return personRepository.findById(compositeKey)
            // 3. Use .map() to transform the Person entity into a PersonResponseDTO.
            .map(this::mapToDTO);

    }
    // --- Collection Lookup ---
    public List<PersonResponseDTO> getPersonsByHearingId(UUID hearingId) {
        return personRepository.findAllById_HearingId(hearingId)
            .stream()
            .map(this::mapToDTO) // Uses the unified helper
            .toList();
    }


    // Private Mapping Helper method
    private PersonResponseDTO mapToDTO(Person personEntity) {
        return new PersonResponseDTO(
            personEntity.getId().getPersonUuid(),
            personEntity.getId().getHearingId(),
            personEntity.getFirstName(),
            personEntity.getLastName()
        );
    }

}