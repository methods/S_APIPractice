package com.example.HearingsDemo.person;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

// blueprint for PersonId Composite Key
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PersonId implements Serializable {

    // Version number: Version 1 
    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    private UUID personUuid;

    @Column(name = "hearing_id")
    private UUID hearingId;
}
