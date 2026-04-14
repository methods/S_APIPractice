package com.example.hearingsdemo.hearingresulteddocument;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HearingDocumentServiceTest {

    @Mock
    private HearingResultedDocumentRepository mockRepository;

    @InjectMocks
    private HearingResultedDocumentService service;

    // ======================================================================
    // A simple utility class for creating test entity objects
    // ======================================================================
    private HearingResultedDocument createEntity(UUID hearingUuid, LocalDate hearingDay, String payload) {
        LocalDate dummyDate = LocalDate.parse("2000-01-01");

        return new HearingResultedDocument(
            new HearingResultedDocumentId(hearingUuid, hearingDay),
            dummyDate,
            dummyDate,
            payload
        );
    }

    @Test
    @DisplayName("getAllDocumentsByHearingId should return a mapped list of DTOs when documents are found")
    void getAllDocumentsByHearingId_shouldReturnMappedDtos_whenDocumentsFound() {

        // Arrange UUID
        UUID hearingUuid = UUID.randomUUID();
        LocalDate day1 = LocalDate.parse("2023-10-30");
        LocalDate day2 = LocalDate.parse("2023-10-31");

        // Create fake entities
        HearingResultedDocument entity1 = createEntity(hearingUuid, day1, "payload1");
        HearingResultedDocument entity2 = createEntity(hearingUuid, day2, "payload2");

        when(mockRepository.findAllByHearingResultedDocumentId_HearingUuid(hearingUuid))
            .thenReturn(List.of(entity1, entity2));

        // ACT
        List<HearingDocumentResponseDTO> results = service.getAllDocumentsByHearingId(hearingUuid);

        // ASSERT
        assertThat(results).hasSize(2);
        assertThat(results.get(0).hearingDay()).isEqualTo(day1);
        assertThat(results.get(0).payload()).isEqualTo("payload1");

        // Verify Mapping
        HearingDocumentResponseDTO resultDto1 = results.get(0);
        assertThat(resultDto1.hearingDay()).isEqualTo(entity1.getHearingResultedDocumentId().getHearingDay());
        assertThat(resultDto1.payload()).isEqualTo(entity1.getPayload());

        HearingDocumentResponseDTO resultDto2 = results.get(1);
        assertThat(resultDto2.hearingDay()).isEqualTo(entity2.getHearingResultedDocumentId().getHearingDay());
        assertThat(resultDto2.payload()).isEqualTo(entity2.getPayload());

    }


    @Test
    @DisplayName("getAllDocumentsByHearingId should return an empty list when no documents are found")
    void getAllDocumentsByHearingId_shouldReturnEmptyList_whenNoDocumentsFound() throws Exception {

        UUID hearingUuid = UUID.randomUUID();

        when(mockRepository.findAllByHearingResultedDocumentId_HearingUuid(hearingUuid)).thenReturn(Collections.emptyList());

        // ACT
        List<HearingDocumentResponseDTO> results = service.getAllDocumentsByHearingId(hearingUuid);

        // ASSERT
        assertThat(results).isNotNull();
        assertThat(results).isEmpty();

    }

}