package com.example.HearingsDemo.defendantgobaccounts;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "defendant_gob_accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DefendantGOBAccount {

    // --- Composite Key ----
    @EmbeddedId
    private DefendantGOBAccountId id;

    // --- Data Fields ---

    @Column(name = "hearing_id", nullable = false, insertable = false, updatable = false)
    private UUID hearingId;

    @Column(name = "account_number", insertable = false, updatable = false)
    private String accountNumber;

    @Column(name = "account_request_time", insertable = false, updatable = false)
    private LocalDateTime accountRequestTime;

    @Column(name = "case_references", nullable = false, insertable = false, updatable = false)
    private String caseReferences;

    @Column(name = "created_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedTime;

    // --- Helper Methods to flatten access ---

    public UUID getMasterDefendantId() {
        return id != null ? id.getMasterDefendantId() : null;
    }

    public UUID getAccountCorrelationId() {
        return id != null ? id.getAccountCorrelationId() : null;
    }

}