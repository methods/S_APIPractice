package com.example.hearingsdemo.hearingResultedDocument;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HearingResultedDocumentController.class)
public class HearingDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HearingResultedDocumentService service;


    // --- The "Happy Path" Test ---
    @Test
    @DisplayName("GET /hearings/{hearingId} should return 200 OK with a list of documents when found")
    void shouldReturnOkWithDocumentsWhenFound() throws Exception {
        // Arrange ids
        UUID hearingUuid = UUID.randomUUID();

        List<HearingDocumentResponseDTO> mockDtos = List.of(
            new HearingDocumentResponseDTO(LocalDate.parse("2023-10-30"), "payload1"),
            new HearingDocumentResponseDTO(LocalDate.parse("2023-10-31"), "payload2")
        );

        when(service.getAllDocumentsByHearingId(hearingUuid)).thenReturn(mockDtos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/hearings/{hearingId}", hearingUuid))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data", hasSize(2))).andExpect(jsonPath("$.totalCount", is(2)))

            .andExpect(jsonPath("$.data[0].hearingDay", is("2023-10-30")))
            .andExpect(jsonPath("$.data[0].payload", is("payload1")))

            .andExpect(jsonPath("$.data[1].hearingDay", is("2023-10-31")))
            .andExpect(jsonPath("$.data[1].payload", is("payload2")));

    }

    // --- Empty Case Test ---
    @Test
    @DisplayName("GET /hearings/{hearingId} should return 200 OK with an empty list when no documents are found")
    void shouldReturnOkWithEmptyListWhenNotFound() throws Exception {
        //Arrange
        UUID hearingUuid = UUID.randomUUID();

        when(service.getAllDocumentsByHearingId(hearingUuid)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/v1/hearings/{hearingId}", hearingUuid))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data", hasSize(0)))
            .andExpect(jsonPath("$.totalCount", is(0)));
    }

}
