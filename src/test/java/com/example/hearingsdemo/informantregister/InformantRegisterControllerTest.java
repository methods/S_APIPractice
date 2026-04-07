package com.example.hearingsdemo.informantregister;

import com.example.hearingsdemo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InformantRegisterController.class)
public class InformantRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InformantRegisterService service;

    @Test
    @DisplayName("GET /api/v1/informant-reporting/{id} - Should return 200 OK and JSON representation when the record exists")
    void shouldReturn200AndDto_whenIdExists() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();

        // create a mockDto
        InformantRegisterDTO mockDTO = new InformantRegisterDTO(
            id,                                         // id
            UUID.randomUUID(),                          // prosecutionAuthorityId
            "PA-001",                                   // prosecutionAuthorityCode
            "OU-99",                                    // prosecutionAuthorityOuCode
            LocalDate.of(2026, 3, 27),                  // registerDate
            UUID.randomUUID(),                          // fileId
            "{\"key\": \"value\"}",                     // payload
            "PROCESSED",                                // status
            LocalDateTime.of(2026, 3, 27, 10, 30),      // processedOn
            UUID.randomUUID(),                          // hearingId
            LocalDateTime.of(2026, 3, 27, 9, 0),        // registerTime
            LocalDateTime.of(2026, 3, 27, 8, 45),       // generatedTime
            LocalDate.of(2026, 3, 27)                   // generatedDate
        );

        when(service.getInformantResultById(id)).thenReturn(mockDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/informant-reporting/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Identifiers
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.prosecutionAuthorityId").exists())
            .andExpect(jsonPath("$.fileId").exists())
            .andExpect(jsonPath("$.hearingId").exists())

            // Strings & Codes
            .andExpect(jsonPath("$.prosecutionAuthorityCode").value("PA-001"))
            .andExpect(jsonPath("$.prosecutionAuthorityOuCode").value("OU-99"))
            .andExpect(jsonPath("$.status").value("PROCESSED"))
            .andExpect(jsonPath("$.payload").value("{\"key\": \"value\"}"))

            // Date Fields (Matching the 2026-03-27 from your mockDTO)
            .andExpect(jsonPath("$.registerDate").value("2026-03-27"))
            .andExpect(jsonPath("$.generatedDate").value("2026-03-27"))

            // DateTime Fields
            // Note: Use .startsWith() if your JSON includes milliseconds you didn't mock
            .andExpect(jsonPath("$.processedOn").value("2026-03-27T10:30:00"))
            .andExpect(jsonPath("$.registerTime").value("2026-03-27T09:00:00"))
            .andExpect(jsonPath("$.generatedTime").value("2026-03-27T08:45:00"));

    }

    @Test
    @DisplayName("GET /api/v1/informant-reporting/{id} - Should return 404 Not Found when the service throws ResourceNotFoundException")
    void shouldReturn404_whenNotFound() throws Exception {
        //Arrange
        UUID id = UUID.randomUUID();

        when(service.getInformantResultById(id))
            .thenThrow(new ResourceNotFoundException(
                "Informant result not found for id: " + id));

        // Act & Assert
        mockMvc.perform(get("/api/v1/informant-reporting/{id}", id))
            .andExpect(status().isNotFound());
    }

}