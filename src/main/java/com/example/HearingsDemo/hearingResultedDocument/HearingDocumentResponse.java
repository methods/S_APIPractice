package com.example.HearingsDemo.hearingResultedDocument;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record HearingDocumentResponse(
    @Schema(description = "The specific day of the hearing this document pertains to.", example = "2023-10-31")
    LocalDate hearingDay,

    @Schema(
        description = "The raw JSON payload of the hearing result. The frontend is expected to parse this string into a JSON object.",
        example = "{\"resultCode\": \"GUILTY\", \"notes\": \"Defendant pleaded guilty to all charges.\"}"
    )
    String payload
) {
}