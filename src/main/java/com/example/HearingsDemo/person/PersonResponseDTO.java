package com.example.HearingsDemo.person;

import java.util.UUID;

public record PersonResponseDTO(
    UUID personId,
    String firstName,
    String lastName) {

}
