package com.example.HearingsDemo.hearingResultedDocument;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HearingResultedDocumentId implements Serializable {

    // Version number: Version 1
    private static final long serialVersionUID = 1L;

    @Column(name = "hearing_id", insertable = false, updatable = false)
    private UUID hearingUuid;

    @Column(name = "hearing_day", insertable = false, updatable = false)
    private LocalDate hearingDay;
}