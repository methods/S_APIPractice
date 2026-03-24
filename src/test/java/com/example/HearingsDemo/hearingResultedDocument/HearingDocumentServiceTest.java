package com.example.HearingsDemo.hearingResultedDocument;

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
    // Test-only helper - A simple utility class for creating test objects
    // ======================================================================
    static class TestUtils {
        public static HearingResultedDocument createHearingDocumentEntity(UUID hearingId, LocalDate hearingDay, String payload) {
            // Using reflection to bypass private constructor for test setup
            try {
                var constructor = HearingResultedDocument.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                var entity = constructor.newInstance();

                var idField = HearingResultedDocument.class.getDeclaredField("hearingResultedDocumentId");
                idField.setAccessible(true);
                idField.set(entity, new HearingResultedDocumentId(hearingId, hearingDay));

                var payloadField = HearingResultedDocument.class.getDeclaredField("payload");
                payloadField.setAccessible(true);
                payloadField.set(entity, payload);

                // We can set other fields if needed for other tests
                var startDateField = HearingResultedDocument.class.getDeclaredField("startDate");
                startDateField.setAccessible(true);
                startDateField.set(entity, hearingDay);

                var endDateField = HearingResultedDocument.class.getDeclaredField("endDate");
                endDateField.setAccessible(true);
                endDateField.set(entity, hearingDay);

                return entity;
            } catch (Exception e) {
                throw new RuntimeException("Failed to create test entity", e);
            }
        }
    }

    @Test
    @DisplayName("getAllDocumentsByHearingId should return a mapped list of DTOs when documents are found")
    void getAllDocumentsByHearingId_shouldReturnMappedDtos_whenDocumentsFound() {

        // Arrange UUID and Fake list - not sure how to do this
        UUID hearingUuid = UUID.randomUUID();
        LocalDate hearingDay_1 = LocalDate.parse("2023-10-30");
        LocalDate hearingDay_2 = LocalDate.parse("2023-10-31");

        // Create some fake entity objects.
        HearingResultedDocument entity1 = TestUtils.createHearingDocumentEntity(hearingUuid, hearingDay_1, "payload1");

        HearingResultedDocument entity2 = TestUtils.createHearingDocumentEntity(hearingUuid, hearingDay_2, "payload2");

        List<HearingResultedDocument> mockEntities = List.of(entity1, entity2);

        when(mockRepository.findByHearingResultedDocumentId_HearingUuid(hearingUuid)).thenReturn(mockEntities);

        // Act
        List<HearingDocumentResponseDTO> results = service.getAllDocumentsByHearingId(hearingUuid);

        // Assert
        assertThat(results).isNotNull();
        assertThat(results).hasSize(2);

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

        when(mockRepository.findByHearingResultedDocumentId_HearingUuid(hearingUuid)).thenReturn(Collections.emptyList());

        // ACT
        List<HearingDocumentResponseDTO> results = service.getAllDocumentsByHearingId(hearingUuid);

        // ASSERT
        assertThat(results).isNotNull();
        assertThat(results).isEmpty();

    }

}