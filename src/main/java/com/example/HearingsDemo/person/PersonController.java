package com.example.HearingsDemo.person;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {

        this.personService = personService;
    }

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
