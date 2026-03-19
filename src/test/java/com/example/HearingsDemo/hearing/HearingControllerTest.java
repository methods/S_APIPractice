package com.example.HearingsDemo.hearing;

import com.example.HearingsDemo.person.Person;
import com.example.HearingsDemo.person.PersonId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HearingController.class)
public class HearingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HearingService hearingService;

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


    @Test
    @DisplayName("Should return 200 OK and hearing details with attendee names")
    void shouldReturnHearingWithAttendeesWhenExists() throws Exception {
        // Arrange
        UUID hearingUuid = UUID.randomUUID();
        UUID p1_Uuid = UUID.randomUUID();

        // 1. Create the new AttendeeDTO objects
        List<AttendeeDTO> attendees = List.of(
            new AttendeeDTO(p1_Uuid, "John", "Doe")
        );

        //2. Create the mock resposne
        HearingResponseDTO mockResponse = new HearingResponseDTO(
            hearingUuid,
            LocalDateTime.now(),
            "BS567",
            "Judge Jay",
            attendees
        );

        // 3. Mock Service
        when(hearingService.getHearingById(hearingUuid))
            .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/hearings/{hearingId}", hearingUuid)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.judgeName").value("Judge Jay"))
            .andExpect(jsonPath("$.attendees").isArray())
            .andExpect(jsonPath("$.attendees[0].firstName").value("John"))
            .andExpect(jsonPath("$.attendees[0].lastName").value("Doe"));

    }

    @Test
    @DisplayName("Should return 404 Not Found when hearing ID does not exist")
    void shouldReturn404WhenHearingNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        when(hearingService.getHearingById(randomId))
            .thenThrow(new ResourceNotFoundException("Hearing not found"));

        mockMvc.perform(get("/api/v1/hearings/{hearingId}", randomId)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @ParameterizedTest(name = "Run {index}: hearingId={0}")
    @CsvSource({
        "not-a-uuid" // Bad Hearing ID, Good Person ID
    })
    void shouldReturn400BadRequest_whenIdsAreNotValidUUID(String hearingUuid ) throws Exception {

        //Act & Assert
        mockMvc.perform(get("/api/v1/hearings/{hearingId}", hearingUuid)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

    }

}