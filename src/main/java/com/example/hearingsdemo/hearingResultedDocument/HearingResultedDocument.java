package com.example.hearingsdemo.hearingResultedDocument;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "hearing_resulted_document")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // <-- Only "Family" (classes in the same package or subclasses) can
// see/use it.
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class HearingResultedDocument {

    // --- Composite key ----
    @EmbeddedId
    private HearingResultedDocumentId hearingResultedDocumentId;

    // --- Data ---
    @Column(name = "start_date", insertable = false, updatable = false)
    private LocalDate startDate;

    @Column(name = "end_date", insertable = false, updatable = false)
    private LocalDate endDate;

    @Column(name = "payload", insertable = false, updatable = false)
    private String payload;


    // --- Manual Equals/HashCode
    @Override // overrides default settings of method with matching name
    public boolean equals(Object o) {

        // Step 1: performance optimization with reference check
        if (this == o) return true;

        // Step 2: Safety and Type Check
        if (!(o instanceof HearingResultedDocument)) return false;

        // Step 3: Casting aka the "translation"
        HearingResultedDocument hearingResultedDocument = (HearingResultedDocument) o;

        // Step 4: The Business Logic (Identity Comparison)
        return hearingResultedDocumentId != null && Objects.equals(hearingResultedDocumentId,
            hearingResultedDocument.getHearingResultedDocumentId());
    }

    @Override
    public int hashCode() {
        // THE Equals and HashCode Contract:
        // We hash ONLY the fields used in equals() to keep them in sync
        return Objects.hash(hearingResultedDocumentId);
    }
}