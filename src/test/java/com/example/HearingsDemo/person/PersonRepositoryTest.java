package com.example.HearingsDemo.person;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.lang.NonNull;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest: Slices the context. Only loads DB-related beans.
// It creates an in-memory database (H2) or uses Testcontainers if configured.
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    // --- Helper Method to create a dummy person 
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
    void shouldSaveAndRetrievePersonWithCompositeKey() {
        // Arrange (Create unique ids)
        UUID personId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        // Create the Entity
        Person person = createPerson(personId, hearingId, "Original");

        // Act
        personRepository.save(person); //Null type safety: The expression of type 'Person' needs unchecked conversion to conform to '@NonNull Person'

        // Prepare the tool to find the data
        PersonId compositeKey = new PersonId(personId, hearingId);
        Optional<Person> foundPerson = personRepository.findById(compositeKey);

        // Assert
        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getFirstName()).isEqualTo("Original");
        // CHANGE 3: Assert against the nested fields in the composite key
        assertThat(foundPerson.get().getId().getPersonUuid()).isEqualTo(personId);
        assertThat(foundPerson.get().getId().getHearingId()).isEqualTo(hearingId);

    }

    // LEARNING NOTE: This test verifies the Hibernate Composite Key mapping logic.
    // While this specific API is read-only (CQRS Query side), we keep this test
    // to ensure the @EmbeddedId is correctly configured for identity.
    @Test
    void shouldUpdateExistingPersonWhenIdMatches() {
        // 1. Arrange
        UUID id = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        Person original = createPerson(id, hearingId, "OriginalName");
        // Save to repository
        personRepository.save(original);

        // 2. Act (Save a NEW object with the SAME key)
        Person updated = createPerson(id, hearingId, "NewPerson");
        // Check state can also be changed correctly
        updated.setAddress1("New Address");
        personRepository.save(updated);

        // 3. Assert
        long count = personRepository.count();
        assertThat(count).isEqualTo(1);

        // 4. Verify the data
        Person retrieved = personRepository.findById(new PersonId(id, hearingId)).get();
        assertThat(retrieved.getFirstName()).isEqualTo("NewPerson");
        assertThat(retrieved.getAddress1()).isEqualTo("New Address");
    }

    // ==================================================================
    // GET partial key tests
    // ==================================================================


    @Test
    void shouldFindAllPersonsByHearingId() {
        // Plan save three people to the db, two with the same hearing id
        // see if the correct two people are returned
        //Arrange
        UUID hearingA = UUID.randomUUID();
        UUID hearingB = UUID.randomUUID();

        // Create Persons via helper method above and save to DB
        Person person1 = createPerson(UUID.randomUUID(), hearingA, "Person1");
        Person person2 = createPerson(UUID.randomUUID(), hearingA, "Person2");
        Person person3 = createPerson(UUID.randomUUID(), hearingB, "Person3");

        // saveAll() instead of save() individually for better performance
        personRepository.saveAll(List.of(person1, person2, person3));

        // Act
        List<Person> results = personRepository.findAllById_HearingId(hearingA);

        // Assert
        assertThat(results).hasSize(2)
            .extracting(p -> p.getId().getHearingId())
            .containsOnly(hearingA);
        assertThat(results).extracting(Person::getFirstName)
            .containsExactlyInAnyOrder("Person1", "Person2");


    }

    @Test
    void shouldFindPersonHistoryAcrossHearings() {
        // 1. Arrange: Create ONE person
        UUID samePersonId = UUID.randomUUID();

        // Create two different hearings
        UUID hearingA = UUID.randomUUID();
        UUID hearingB = UUID.randomUUID();

        // add same person to multiple hearings
        personRepository.save(createPerson(samePersonId, hearingA, "SamePerson"));
        personRepository.save(createPerson(samePersonId, hearingB, "SamePerson"));

        // Act: Try to find them just by using the person's UUID ID
        List<Person> history = personRepository.findAllById_PersonUuid(samePersonId);

        // Assert
        assertThat(history).hasSize(2)
            .extracting(p -> p.getId().getPersonUuid())
            .containsOnly(samePersonId);
        assertThat(history).extracting(p -> p.getId().getHearingId())
            .containsExactlyInAnyOrder(hearingA, hearingB);

    }


}