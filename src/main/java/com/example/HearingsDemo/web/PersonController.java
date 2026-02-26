package com.example.HearingsDemo.web;

import com.example.HearingsDemo.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController  // Both @Controller and @ResponseBody: tells Spring this class handles HTTP requests
@RequestMapping("/api/v1/persons") // Tells Spring these HTTP requests sent to this endpoint should be handled by this controller (or method)
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        // This controller depends on a service, and Spring supplies the already-created one (its its own class personService, hence already-created) and "this" personService is the one above so assings it (?)
        this.personService = personService;
    }

    @GetMapping("/{personId}") // <-- maps to /api/v1/persons/{personId}
    // @ResponseEntity controls what and how the resposne is send
    public ResponseEntity<PersonResponseDTO> getPerson(@PathVariable UUID personId) { // @PathVariable takes value from URL
        return null; // Deliberately null to watch the test fail!
    }
    
}
