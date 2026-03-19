package com.example.HearingsDemo.hearing;

import com.example.HearingsDemo.person.Person;
import com.example.HearingsDemo.person.PersonId;
import com.example.HearingsDemo.person.PersonRepository;
import com.example.HearingsDemo.person.PersonService;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HearingServiceTest {

    @Mock
    private HearingRepository hearingRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private HearingService hearingService;

    // Helper to create dummy Hearing
    @NonNull
    private Hearing createHearing(UUID hearingUuid, UUID personUuid) {

        // Create comp key
        HearingId compositeKey = new HearingId(hearingUuid, personUuid);

        // Create Hearing Entity and set values
        Hearing h = new Hearing();
        h.setId(compositeKey);
        h.setStartDate(LocalDateTime.now());
        h.setCourtCentreName("B345");
        h.setJudgeName("Judge Bollinger");

        return h;

    }

    // Helper to create dummy Person
    // Helper copied from PersonService test (DAMP approach)
    @NonNull
    private Person createPerson(UUID personId, UUID hearingId, String firstName, String lastName) {

        // Create the composite key object
        PersonId compositeKey = new PersonId(personId, hearingId);

        // Create Entity and set single composite key
        Person p = new Person();
        p.setId(compositeKey);
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setDateOfBirth(LocalDateTime.now());
        return p;
    }


    // ================================================================
    // Single lookup tests
    // ================================================================

    @Test
    @DisplayName("Should group multiple database rows into a single HearingResponseDTO")
    void shouldReturnSingleHearingWithMultipleAttendees() {

        // 1. Arrange IDs
        UUID commonHearingUuid = UUID.randomUUID();
        UUID p1_Uuid = UUID.randomUUID();
        UUID p2_Uuid = UUID.randomUUID();

        // 2. Arrange Hearing Rows (What's in the 'hearing' table)
        List<Hearing> mockHearingRows = List.of(
            createHearing(commonHearingUuid, p1_Uuid),
            createHearing(commonHearingUuid, p2_Uuid)
        );

        when(hearingRepository.findAllById_HearingUuid(commonHearingUuid)).thenReturn(mockHearingRows);

        // 3. Arrange Person Entities (What's in the 'person' table)
        Person person1 = createPerson(p1_Uuid, commonHearingUuid, "John", "Doe");
        Person person2 = createPerson(p2_Uuid, commonHearingUuid, "Jane", "Smith");

        // 4. Arrange the batch fetch mock
        // We use PersonId because that is the Primary Key of the Person table
        List<PersonId> expectedIds = List.of(
            new PersonId(p1_Uuid, commonHearingUuid),
            new PersonId(p2_Uuid, commonHearingUuid)
        );

        when(personRepository.findAllById(expectedIds)).thenReturn(List.of(person1, person2));
        /*

         */

        // Act: we expect ONE DTO back, not a list of DTOs
        HearingResponseDTO result = hearingService.getHearingById(commonHearingUuid);

        // Assert
        assertThat(result.hearingId()).isEqualTo(commonHearingUuid);
        assertThat(result.judgeName()).isEqualTo("Judge Bollinger");
        assertThat(result.attendees())
            .extracting(AttendeeDTO::firstName, AttendeeDTO::lastName)
            .containsExactlyInAnyOrder(
            tuple("John", "Doe"),
            tuple("Jane", "Smith")
            );

    }

    @Test
    @DisplayName("Should throw resourceNotFoundException when no rows exist for the given UUID")
    void shouldThrowNotFoundWhenHearingIdDoesntExist() throws Exception {
        UUID randomHearingUuid = UUID.randomUUID();

        when(hearingRepository.findAllById_HearingUuid(randomHearingUuid)).thenReturn(List.of());

        assertThatThrownBy(() -> hearingService.getHearingById(randomHearingUuid)).isInstanceOf(ResourceNotFoundException.class);

    }




}