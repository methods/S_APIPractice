package com.example.HearingsDemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

// blureprint for PersonId Composite Key
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonId implements Serializable {
    private UUID id;
    private UUID hearingId;
}
