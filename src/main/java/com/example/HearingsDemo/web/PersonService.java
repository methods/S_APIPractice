package com.example.HearingsDemo.web;

import com.example.HearingsDemo.domain.Person;
import com.example.HearingsDemo.domain.PersonId;
import com.example.HearingsDemo.domain.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public Optional<PersonResponseDTO> getPersonById(UUID hearingId, UUID personId) {
        // 1. Instantiate comp key
        PersonId compositeKey = new PersonId(personId, hearingId);

        // 2. Call repo to find the data
        return personRepository.findById(compositeKey)
            // 3. Use .map() to transform the Person entity into a PersonResponseDTO.
            .map(this::mapToDTO);

    }

    // Private Mapping Helper method
    private PersonResponseDTO mapToDTO(Person personEntity) {
        return new PersonResponseDTO(
            personEntity.getId().getPersonUuid(),
            personEntity.getFirstName(),
            personEntity.getLastName()
        );
    }

}