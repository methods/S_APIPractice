package com.example.HearingsDemo.person;

import java.util.UUID;

public record PersonResponseDTO(
    UUID personId,
    UUID hearingId,
    String firstName,
    String lastName) {

}
