package com.example.HearingsDemo.person;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PersonCollectionResponseDTO(
    @Schema(
        description = "List of persons found for the hearing",
        example = "[{\"personUuid\": \"550e8400-e29b-41d4-a716-446655440000\", \"firstName\": \"John\", \"lastName\": \"Doe\"}]"
    )
    List<PersonResponseDTO> persons,

    @Schema(description = "The total number of persons associated with this hearing", example = "5")
    int totalCount
) {

}