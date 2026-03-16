package com.example.HearingsDemo.person;

import java.util.List;

public record PersonCollectionResponseDTO(
    List<PersonResponseDTO> persons,
    int totalCount
) {

}