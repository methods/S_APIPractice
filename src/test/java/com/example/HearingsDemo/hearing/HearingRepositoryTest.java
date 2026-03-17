package com.example.HearingsDemo.hearing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HearingRepositoryTest {

    @Autowired
    private HearingRepository hearingRepository;

    // Helper to create dummy Hearing
    @NonNull
    private Hearing createHearing(UUID hearingUuid, UUID personUuid) {

        // Create comp key
        HearingId compositeKey = new HearingId(hearingUuid, personUuid);

        // Create Hearing Entity and set values
        Hearing h = new Hearing();
        h.setId(compositeKey);
        h.setStartDate(LocalDateTime.now());
        h.setCourtCentreName("Test Court Centre");
        h.setCourtCode("EH3TH");
        h.setJudgeName("Test Judge");
        h.setProsecutorName("Test_prosecutor");
        h.setDefenceName("Test defence");

        return h;

    }

    // ===========================================================
    // Single lookup
    // ===========================================================
    @Test
    void shouldSaveAndRetrieveHearingWithCompoSiteKey() {

        // Arrange
        UUID hearingUuid = UUID.randomUUID();
        UUID personUuid = UUID.randomUUID();

        // Create the hearing Entity + save to database
        Hearing hearing = createHearing(hearingUuid, personUuid);
        hearingRepository.save(hearing);

        // Act
        HearingId compositeKey = new HearingId(hearingUuid, personUuid);
        Optional<Hearing> foundHearing = hearingRepository.findById(compositeKey);

        // Assert
        assertThat(foundHearing).isPresent();
        assertThat(foundHearing.get().getId().getHearingUuid()).isEqualTo(hearingUuid);
        assertThat(foundHearing.get().getId().getPersonUuid()).isEqualTo(personUuid);


    }


}