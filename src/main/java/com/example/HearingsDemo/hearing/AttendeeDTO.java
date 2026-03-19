package com.example.HearingsDemo.hearing;


import java.util.UUID;

public record AttendeeDTO(
    UUID personId,
    String firstName,
    String lastName
) {}
