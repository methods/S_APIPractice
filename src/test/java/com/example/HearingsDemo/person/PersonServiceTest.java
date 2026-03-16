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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    // ================================================================
    // Single lookup tests
    // ================================================================

    @Test
    @DisplayName("Should return person DTO when person and hearing id matches (composite key match)")
    void shouldReturnPersonDtoCompositeKeyMatch() {
        // Arrange
        UUID personUuid = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        PersonId compositeKey = new PersonId(personUuid, hearingId);

        // Create the "Database" version of the person
        Person mockEntity = createPerson(personUuid, hearingId, "John");

        // Arrange mock
        when(personRepository.findById(compositeKey)).thenReturn(Optional.of(mockEntity));

        // Act
        Optional<PersonResponseDTO> result = personService.getPersonById(personUuid, hearingId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().firstName()).isEqualTo("John");
        assertThat(result.get().personId()).isEqualTo(personUuid);

        // Verify the repo was actually called exactly once
        verify(personRepository, times(1)).findById(compositeKey);

    }

    @Test
    @DisplayName("Should return empty Optional when person not found")
    void shouldReturnEmptyWhenNoCompositeKeyMatch() {
        // Arrange
        UUID personUuid = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        PersonId compositeKey = new PersonId(personUuid, hearingId);

        when(personRepository.findById(compositeKey)).thenReturn(Optional.empty());

        // Act and Assert
        Optional<PersonResponseDTO> result = personService.getPersonById(personUuid, hearingId);
        assertThat(result).isEmpty();
        verify(personRepository).findById(compositeKey);

    }


    // ================================================================
    // Collection lookup tests
    // ================================================================

    @Test
    @DisplayName("Should return a collection wrapper when people exist for a hearing")
    void shouldReturnCollectionWrapperForHearing() {

        // Arrange: create a person
        UUID hearingId = UUID.randomUUID();
        Person mockEntity = createPerson(UUID.randomUUID(), hearingId, "John");

        when(personRepository.findAllById_HearingId(hearingId)).thenReturn(List.of(mockEntity));

        // Act
        PersonCollectionResponseDTO result = personService.getPersonsByHearingId(hearingId);

        // Assert
        assertThat(result.totalCount()).isEqualTo(1);
        assertThat(result.persons()).hasSize(1);
        assertThat(result.persons().get(0).firstName()).isEqualTo("John");
        assertThat(result.persons().get(0).hearingId()).isEqualTo(hearingId);

    }

}