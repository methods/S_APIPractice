package com.example.HearingsDemo.hearing;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

// blueprint for HearingId Composite Key
// Used @Data as this is a "Value Object" and doesn't have a lifecycle
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HearingId implements Serializable {

    // Version number: Version 1
    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    private UUID hearingUuid;

    @Column(name = "person_id")
    private UUID personUuid;

}
