package com.example.HearingsDemo.web;

import java.util.UUID;

public record PersonResponseDTO(
    UUID personId,
    String firstName,
    String lastName) {

}
