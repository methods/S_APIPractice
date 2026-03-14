package com.example.HearingsDemo.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean; // <-- Fakes service layer: swaps real
// spring bean with mockito mock bean for tests
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc; // <-- Simulates HTTP calls

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Test
    @DisplayName("Should return 200 OK and person details when person exists")
    void shouldReturnPersonWhenExists() throws Exception {
        
        // Arrange: create a mock DTO Person response object
        UUID hearingId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        PersonResponseDTO mockResponse = new PersonResponseDTO(
            personId, hearingId, "John", "Doe"
        );
        
        // Mock service layer to return the mock DTO created above
        when(personService.getPersonById(personId, hearingId))
            .thenReturn(Optional.of(mockResponse));

        // Act & Assert
        mockMvc.perform(get("/api/v1/hearings/{hearingId}/persons/{personId}",
            hearingId, personId)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
    
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        .andExpect(jsonPath("$.personId").value(personId.toString()))

        .andExpect(jsonPath("$.firstName").value("John"))
        
        .andExpect(jsonPath("$.lastName").value("Doe"));

    }

    @ParameterizedTest(name = "Run {index}: hearingId={0}, personId={1}")
    @CsvSource({
        "not-a-uuid, 123e4567-e89b-12d3-a456-426614174000", // Bad Hearing ID, Good Person ID
        "123e4567-e89b-12d3-a456-426614174000, not-a-uuid", // Good Hearing ID, Bad Person ID
        "invalid-hearing, invalid-person"                   // Both Bad
    })
    void shouldReturn400BadRequest_whenIdsAreNotValidUUIDs(String hearingIdParam, String personIdParam) throws Exception {

        //Act & Assert
        mockMvc.perform(get("/api/v1/hearings/{hearingId}/persons/{personId}", hearingIdParam, personIdParam)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());


    }

    @Test
    void shouldReturn404NotFound_whenPersonDoesNotExist() throws Exception {
        when(personService.getPersonById(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/hearings/{hearingId}/persons/{personId}", UUID.randomUUID(), UUID.randomUUID())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    // ================================
    // GET /by hearingId
    // ================================

    @Test
    @DisplayName("Should return 200 OK and a list of persons for a hearing")
    void shouldReturnListOfPersonsWhenHearingExists() throws Exception {

        // 1. Arrange
        UUID hearingId = UUID.randomUUID();
        PersonResponseDTO p1 = new PersonResponseDTO(UUID.randomUUID(), hearingId, "John", "Doe");
        PersonResponseDTO p2 = new PersonResponseDTO(UUID.randomUUID(), hearingId, "Jane", "Smith");

        when(personService.getPersonsByHearingId(hearingId)).thenReturn(List.of(p1, p2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/hearings/{hearingId}/persons", hearingId)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].firstName").value("John"))
            .andExpect(jsonPath("$[1].firstName").value("Jane"));

    }

}
