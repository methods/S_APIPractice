package com.example.HearingsDemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

// blueprint for PersonId Composite Key
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonId implements Serializable {

    // Version number: Version 1 
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID hearingId;
}
