package com.example.hearingsdemo.hearingResultedDocument;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * This record acts as the top-level wrapper for our JSON response.
 * It makes our API extensible for future features like pagination.
 * @param data
 */
public record HearingDocumentCollectionResponseDTO(

    @Schema(
        description = "List of documents found for the hearing",
        example = """
            [
              {
                "hearingDay": "2023-10-31",
                "payload": "{\\"result\\": \\"Success\\"}"
              }
            ]
            """
    )
    List<HearingDocumentResponseDTO> data,

    @Schema(description = "The total number of documents associated with this hearing", example = "5")
    int totalCount
) {

}