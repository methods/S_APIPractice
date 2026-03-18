package com.example.HearingsDemo.hearing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HearingServiceTest {

    @Mock
    private HearingRepository hearingRepository;

    @InjectMocks
    private HearingService hearingService;

    // Helper to create dummy Hearing
    @NonNull
    private Hearing createHearing(UUID hearingUuid, UUID personUuid) {

        // Create comp key
        HearingId compositeKey = new HearingId(hearingUuid, personUuid);

        // Create Hearing Entity and set values
        Hearing h = new Hearing();
        h.setId(compositeKey);
        h.setStartDate(LocalDateTime.now());
        h.setCourtCentreName("B345");
        h.setJudgeName("Judge Bollinger");

        return h;

    }

    // ================================================================
    // Single lookup tests
    // ================================================================

    @Test
    @DisplayName("Should group multiple database rows into a single HearingResponseDTO")
    void shouldReturnSingleHearingWithMultipleAttendees() {

        // Arrange: create and save a couple of rows where hearingUuid is the same but personUuid is not
        UUID commonHearingUuid = UUID.randomUUID();
        UUID p1_Uuid = UUID.randomUUID();
        UUID p2_Uuid = UUID.randomUUID();

        List<Hearing> mockRows = List.of(
            createHearing(commonHearingUuid, p1_Uuid),
            createHearing(commonHearingUuid, p2_Uuid)
        );

        // Create Entity and save to Db
        when(hearingRepository.findAllById_HearingUuid(commonHearingUuid)).thenReturn(mockRows);

        // Act: we expect ONE DTO back, not a list of DTOs
        HearingResponseDTO result = hearingService.getHearingById(commonHearingUuid);

        // Assert
        assertThat(result.hearingId()).isEqualTo(commonHearingUuid);
        assertThat(result.judgeName()).isEqualTo("Judge Bollinger");
        assertThat(result.attendeeIds()).hasSize(2).containsExactlyInAnyOrder(p1_Uuid, p2_Uuid);

    }

    @Test
    @DisplayName("Should throw resourceNotFoundException when no rows exist for the given UUID")
    void shouldThrowNotFoundWhenHearingIdDoesntExist() throws Exception {
        UUID randomHearingUuid = UUID.randomUUID();

        when(hearingRepository.findAllById_HearingUuid(randomHearingUuid)).thenReturn(List.of());

        assertThatThrownBy(() -> hearingService.getHearingById(randomHearingUuid)).isInstanceOf(ResourceNotFoundException.class);

    }


}