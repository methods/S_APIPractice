package com.example.HearingsDemo.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
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
    private Person createPerson(UUID pID, UUID hID, String firstName) {
        Person p = new Person();
        p.setId(pID);
        p.setHearingId(hID);
        p.setFirstName(firstName);
        p.setLastName("TestUser");
        p.setDateOfBirth(LocalDateTime.now());
        return p; 
    }

    @Test
    void shouldSaveAndRetrievePersonWithCompositeKey() {
        // 1.1. Arrange (Create unique ids)
        UUID personId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        
        // 1.2. Create the Entity
        Person person = createPerson(personId, hearingId, "Original");

        // 2. Act (Try to save it)
        personRepository.save(person);

        // 3. Assert
        // Step A: Prepare the tool to find the data
        PersonId compositeKey = new PersonId(personId, hearingId);
        // Step 2: Use the tool
        Optional<Person> foundPerson = personRepository.findById(compositeKey);

        // Assertions (AssertJ library)
        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getFirstName()).isEqualTo("Original");
        assertThat(foundPerson.get().getId()).isEqualTo(personId);
        assertThat(foundPerson.get().getHearingId()).isEqualTo(hearingId);
    }
}