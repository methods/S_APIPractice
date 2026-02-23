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

    @Test
    void shouldSaveAndRetrievePersonWithCompositeKey() {
        // 1. Arrange (Create the Data)
        UUID personId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();

        // Create the Entity
        Person person = new Person();
        person.setId(personId);
        person.setHearingId(hearingId);
        person.setFirstName("TDD");
        person.setLastName("User");
        person.setDateOfBirth(LocalDateTime.of(2026, 2, 23, 12, 0));
        person.setAddress1("123");
        person.setAddress2("Fake Street");
        person.setAddress3("Bulming");
        person.setAddress4("Wyoming");
        person.setPostCode("FH5 JHY");

        // 2. Act (Try to save it)
        personRepository.save(person);

        // 3. Assert (Check if it worked)
        PersonId compositeKey = new PersonId(personId, hearingId);
        
        // Try to find it
        Optional<Person> foundPerson = personRepository.findById(compositeKey);

        // Assertions (AssertJ library)
        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getFirstName()).isEqualTo("TDD");
        assertThat(foundPerson.get().getAddress1()).isEqualTo("123");
        assertThat(foundPerson.get().getId()).isEqualTo(personId);
    }
}