package com.example.HearingsDemo.person;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PersonCollectionResponseDTO(
    @Schema(description = "List of persons found for the hearing", example = "[]")
    List<PersonResponseDTO> persons,

    @Schema(description = "The total number of persons associated with this hearing", example = "5")
    int totalCount
) {

}