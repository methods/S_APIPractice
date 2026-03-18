package com.example.HearingsDemo.hearing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
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

    @Test
    @DisplayName("Should return 200 OK and hearing details when ID exists")
    void shouldReturnHearingWhenExists() throws Exception {
        // Arrange
        UUID hearingUuid = UUID.randomUUID();
        // Create attendeeId List
        List<UUID> attendeeIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        HearingResponseDTO mockResponse = new HearingResponseDTO(
            hearingUuid,
            LocalDateTime.now(),
            "BS567",
            "Judge Jay",
            attendeeIds
        );

        // MOck Service
        when(hearingService.getHearingById(hearingUuid))
            .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/hearings/{hearingId}", hearingUuid)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.judgeName").value("Judge Jay"))
            .andExpect(jsonPath("$.attendeeIds").isArray())
            .andExpect(jsonPath("$.attendeeIds", hasSize(2)));

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