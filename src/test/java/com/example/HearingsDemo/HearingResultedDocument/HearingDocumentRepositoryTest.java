package com.example.HearingsDemo.hearingResultedDocument;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HearingDocumentRepositoryTest {

    @Autowired
    private HearingResultedDocumentRepository repository;

    // ===========================================================
    // Helper Methods
    // ===========================================================

    // A helper record to make test data clean and immutable.
    private record TestDoc(UUID hearingUuid, LocalDate hearingDay, String payload) {}

    // Test-only helper method to create entities since our main entity has no public constructor
    // We are using reflection (which is what JPA does) to create the object for the test.
    // This is how we can create an instance of a class with no public constructor.
    private HearingResultedDocument createEntity(TestDoc data) {
        var id = new HearingResultedDocumentId(data.hearingUuid(), data.hearingDay());

        try {
            var constructor = HearingResultedDocument.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            var entity = constructor.newInstance();
            var idField = HearingResultedDocument.class.getDeclaredField("hearingResultedDocumentId");
            idField.setAccessible(true);
            idField.set(entity, id);
            var payloadField = HearingResultedDocument.class.getDeclaredField("payload");
            payloadField.setAccessible(true);
            payloadField.set(entity, data.payload());
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ===========================================================
    // Partial Key lookup
    // ===========================================================

    @Test
    void shouldFindAllDocumentsForGivenHearingUuidAndIgnoreOthers() {

        // ARRANGE - Set up the test data
        UUID targetHearingUuid = UUID.randomUUID();
        UUID otherHearingUuid = UUID.randomUUID();

        var doc1_day1 = new TestDoc(targetHearingUuid, LocalDate.parse("2023-10-30"), "payload1");
        var doc1_day2 = new TestDoc(targetHearingUuid, LocalDate.parse("2023-10-31"), "payload2");
        var doc2_day1 = new TestDoc(otherHearingUuid, LocalDate.parse("2023-11-01"), "payload3");

        // Due to CQRS structure, we can't use constructors on our @Entity. Data must be inserted using a test-only
        // approach (test data builder or SQL scripts.
        // For now, we will manually create the entities.
        repository.saveAll(List.of(
            createEntity(doc1_day1),
            createEntity(doc1_day2),
            createEntity(doc2_day1)
        ));

        // ACT
        List<HearingResultedDocument> results =
            repository.findByHearingResultedDocumentId_HearingUuid(targetHearingUuid);

        // Assert
        assertThat(results).isNotNull();
        assertThat(results).hasSize(2);
        assertThat(results)
            .extracting(doc -> doc.getHearingResultedDocumentId().getHearingDay()).containsExactlyInAnyOrder(doc1_day1.hearingDay(), doc1_day2.hearingDay());

    }
}