package com.example.HearingsDemo.person;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Person Validation", description = "Endpoints for verifying Person data integrity")
@RestController
@RequestMapping("/api/v1")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {

        this.personService = personService;
    }

    @Operation(summary = "Fetch a Person by Hearing ID and Person ID", description = "Retrieves the specific record of a person attending a specific hearing, based on their unique identifiers.")
    @GetMapping("/hearings/{hearingId}/persons/{personId}")
    public ResponseEntity<PersonResponseDTO> getPersonById(
        @PathVariable UUID hearingId,
        @PathVariable UUID personId) { //
        // @PathVariable takes value
        // 1. Call the service
        return personService.getPersonById(personId, hearingId)
            // 2. If data exists, wrap it in HTTP 200 OK
            .map(ResponseEntity::ok)
            // 3. If empty (Optional.empty), return HTTP 404 Not Found
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List all Persons associated with a specific Hearing", description =
        "Retrieves a list of all individuals scheduled for a specific hearing, including a total count for easy validation.")
    @GetMapping("/hearings/{hearingId}/persons")
    public ResponseEntity<PersonCollectionResponseDTO> getPersonsByHearingId(
        @PathVariable UUID hearingId
    ) {
        // Call service method that returns a PersonCollectionResponseDTO
        PersonCollectionResponseDTO personsList = personService.getPersonsByHearingId(hearingId);

        // Return 200 OK even if List is empty
        return ResponseEntity.ok(personsList);
    }
    
}
