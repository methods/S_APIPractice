package com.example.HearingsDemo.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    // Create mocks: a mock database
    @Mock
    private PersonRepository personRepository;

    // Inject mocks to this
    @InjectMocks
    private PersonService personService;

    // Helper copied from Repo test (DAMP approach)
    @NonNull
    private Person createPerson(UUID personId, UUID hearingId, String firstName) {

        // Create the composite key object
        PersonId compositeKey = new PersonId(personId, hearingId);

        // Create Entity and set single composite key
        Person p = new Person();
        p.setId(compositeKey);
        p.setFirstName(firstName);
        p.setLastName("TestUser");
        p.setDateOfBirth(LocalDateTime.now());
        return p;
    }

    @Test
    @DisplayName("Should return a list of DTOs when people exist for a hearing")
    void shouldReturnDtoListForHearing() {

        // Arrange: create a person
        UUID hearingId = UUID.randomUUID();
        Person mockPerson = createPerson(UUID.randomUUID(), hearingId, "John");

        // Instruct mock
        when(personRepository.findAllById_HearingId(hearingId)).thenReturn(List.of(mockPerson));

        // Act
        List<PersonResponseDTO> results = personService.getPersonsByHearingId(hearingId);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).firstName()).isEqualTo("John");
        assertThat(results.get(0).hearingId()).isEqualTo(hearingId);

    }

}