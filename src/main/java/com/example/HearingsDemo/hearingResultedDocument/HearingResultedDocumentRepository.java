package com.example.HearingsDemo.hearingResultedDocument;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface HearingResultedDocumentRepository  extends JpaRepository<HearingResultedDocument,
    HearingResultedDocumentId> {

    /**
     * Finds all HearingResultedDocument entities for a given hearing UUID.

     * @param hearingUuid The UUID of the hearing to search for.
     * @return A list of all documents for all days of that hearing.
     */

    List<HearingResultedDocument> findByHearingResultedDocumentId_HearingUuid(UUID hearingUuid);

}
