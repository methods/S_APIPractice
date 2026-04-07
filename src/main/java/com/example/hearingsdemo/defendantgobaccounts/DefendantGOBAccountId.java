package com.example.hearingsdemo.defendantgobaccounts;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DefendantGOBAccountId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "master_defendant_id",  insertable = false, updatable = false)
    private UUID masterDefendantId;

    @Column(name = "account_correlation_id",  insertable = false, updatable = false)
    private UUID accountCorrelationId;
}