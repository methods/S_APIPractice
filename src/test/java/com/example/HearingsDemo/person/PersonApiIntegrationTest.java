package com.example.HearingsDemo.person;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Inject the real repository to set up data
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void shouldReturnPersonDetailsWhenRecordExistsInDatabase() throws Exception {
        // Arrange
        // Create and send REAL entity to the test database
        UUID personId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();

        PersonId compositeKey = new PersonId(personId, hearingId);
        Person testPerson = new Person();
        testPerson.setId(compositeKey);
        testPerson.setFirstName("Jane");
        testPerson.setLastName("Doe");

        personRepository.save(testPerson);
        entityManager.flush();
        entityManager.clear();

        // Act & Assert
        // Make a REAL HTTP call to the endpoint
        mockMvc.perform(get("/api/v1/hearings/{hearingId}/persons/{personId}", hearingId, personId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.personId").value(personId.toString()))
            .andExpect(jsonPath("$.firstName").value("Jane"))
            .andExpect(jsonPath("$.lastName").value("Doe"));
    }


    @Test
    void shouldReturn404NotFoundWhenRecordDoesNotExist() throws Exception {
        // Act & Assert: Call the endpoint with random IDs that don't exist
        mockMvc.perform(get("/api/v1/hearings/{hearingId}/persons/{personId}", UUID.randomUUID(), UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    // ================================
    // GET /by hearingId
    // ================================
    @Test
    void shouldReturnEmptyListWhenNoPersonsExistForHearing() throws Exception {
        mockMvc.perform(get("/api/v1/hearings/{hearingId}/persons", UUID.randomUUID()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.persons.size()").value(0))
            .andExpect(jsonPath("$.totalCount").value(0));
    }

    @Test
    @DisplayName("Should return a list of persons for a specific hearing from the database")
    void shouldReturnCollectionFromDatabase() throws Exception {
        // Arrange
        UUID hearingA = UUID.randomUUID();
        UUID hearingB = UUID.randomUUID();

        // Two people in Hearing A
        personRepository.save(createEntity(UUID.randomUUID(), hearingA, "John"));
        personRepository.save(createEntity(UUID.randomUUID(), hearingA, "Jane"));

        // One person in Hearing B (to test filtering)
        personRepository.save(createEntity(UUID.randomUUID(), hearingB, "Isolated"));

        // Force Hibernate to write to db not save it in cache
        entityManager.flush();
        entityManager.clear();

        // Act & Assert
        mockMvc.perform(get("/api/v1/hearings/{hearingId}/persons", hearingA)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.persons.size()").value(2))
            .andExpect(jsonPath("$.totalCount").value(2))

            .andExpect(jsonPath("$.persons[*].firstName").value(org.hamcrest.Matchers.containsInAnyOrder("John", "Jane")))
            .andExpect(jsonPath("$.persons[*].hearingId").value(org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is(hearingA.toString()))));

    }

    private Person createEntity(UUID pId, UUID hId, String name) {
        Person p = new Person();
        p.setId(new PersonId(pId, hId));
        p.setFirstName(name);
        return p;
    }

}