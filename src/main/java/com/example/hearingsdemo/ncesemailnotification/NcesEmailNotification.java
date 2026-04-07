package com.example.hearingsdemo.ncesemailnotification;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Entity
@Immutable
@Table(name = "nces_email_notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NcesEmailNotification {
    // --- Primary key ---
    @Id
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private UUID id;

    // --- Data fields ---

    @Column(name = "material_id", insertable = false, updatable = false)
    private UUID materialId;

    @Column(name = "notification_id", insertable = false, updatable = false)
    private UUID notificationId;

    @Column(name = "master_defendant_id", insertable = false, updatable = false)
    private UUID masterDefendantId;

    @Column(name = "send_to", insertable = false, updatable = false)
    private String sendTo;

    @Column(name = "subject", insertable = false, updatable = false)
    private String subject;

}