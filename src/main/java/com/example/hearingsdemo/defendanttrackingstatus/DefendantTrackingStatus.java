package com.example.hearingsdemo.defendanttrackingstatus;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "defendant_tracking_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DefendantTrackingStatus {

    // ---- Primary key ----
    @Id
    @Column(name = "offence_id", insertable = false, updatable = false)
    private UUID offenceId;

    // --- Data ---
    @Column(name = "defendant_id", insertable = false, updatable = false)
    private UUID defendantId;

    @Column(name = "em_status", insertable = false, updatable = false)
    private Boolean emStatus;

    @Column(name = "em_last_modified_time", insertable = false, updatable = false)
    private LocalDateTime emLastModifiedTime;

    @Column(name = "woa_status", insertable = false, updatable = false)
    private Boolean woaStatus;

    @Column(name = "woa_last_modified_time", insertable = false, updatable = false)
    private LocalDateTime woaLastModifiedTime;
}
