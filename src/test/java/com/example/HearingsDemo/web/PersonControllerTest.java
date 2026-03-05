package com.example.HearingsDemo.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean; // <-- Fakes service layer: swaps real
// spring bean with mockito mock bean for tests
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc; // <-- Simulates HTTP calls

import java.util.Optional;
import java.util.UUID;

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
            personId, "John", "Doe"
        );
        
        // Mock service layer to return the mock DTO created above
        Mockito.when(personService.getPersonById(hearingId, personId))
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
    
}
