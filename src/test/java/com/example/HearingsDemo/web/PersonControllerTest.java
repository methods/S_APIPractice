package com.example.HearingsDemo.web;

// Dont have the two below yet
import com.example.HearingsDemo.web.PersonResponseDTO;
import com.example.HearingsDemo.service.PersonService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
// slice test for loading ONLY wen layer, not whole application
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// Fakes service layer: swaps real spring bean with mockito mock bean for tests
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
// Simulates HTTP calls
import org.springframework.test.web.servlet.MockMvc;

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
    @DisplayName("Should return 200 OK and person details when person exists") // <--- previously we didn't use this annotation- why?
    void shouldReturnPersonWhenExists() throws Exception {
        
        // Arrange: create a mock DTO Person response object
        UUID personId = UUID.randomUUID();
        PersonResponseDTO mockResponse = new PersonResponseDTO(
            personId, "John", "Doe"
        );
        
        // Mock service layer to return the mock DTO created above
        // personId needs to be converted to string as HTTP protocol is only string type
        Mockito.when(personService.getPersonById(personId.toString())).thenReturn(Optional.of(mockResponse));

        // Act & Assert
        mockMvc.perform(get("/api/v1/persons/{personId}", personId.toString()) // personId needs to be converted to string as HTTP protocol is only string type
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
    
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        .andExpect(jsonPath("$.personId").value("12345"))

        .andExpect(jsonPath("$.firstName").value("John"))
        
        .andExpect(jsonPath("$.lastName").value("Doe"));

    }
    
}
