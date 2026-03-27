package com.example.HearingsDemo.informantregister;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "informant_register")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class InformantRegister {

    @Id
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private UUID id;

    @Column(name = "prosecution_authority_id", nullable = false, insertable = false, updatable = false)
    private UUID prosecutionAuthorityId;

    @Column(name = "prosecution_authority_code", insertable = false, updatable = false)
    private String prosecutionAuthorityCode;

    @Column(name = "prosecution_authority_ou_code", insertable = false, updatable = false)
    private String prosecutionAuthorityOuCode;

    @Column(name = "register_date", nullable = false, insertable = false, updatable = false)
    private LocalDate registerDate;

    @Column(name = "file_id", insertable = false, updatable = false)
    private UUID fileId;

    @Column(name = "payload", nullable = false, insertable = false, updatable = false)
    private String payload;

    @Column(name = "status", insertable = false, updatable = false)
    private String status;

    @Column(name = "processed_on", insertable = false, updatable = false)
    private LocalDateTime processedOn;

    @Column(name = "hearing_id", insertable = false, updatable = false)
    private UUID hearingId;

    @Column(name = "register_time", insertable = false, updatable = false)
    private LocalDateTime registerTime;

    @Column(name = "generated_time", insertable = false, updatable = false)
    private LocalDateTime generatedTime;

    @Column(name = "generated_date", insertable = false, updatable = false)
    private LocalDate generatedDate;
}