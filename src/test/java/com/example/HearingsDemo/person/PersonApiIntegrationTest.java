package com.example.HearingsDemo.person;

import jakarta.persistence.EntityManager;
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

}