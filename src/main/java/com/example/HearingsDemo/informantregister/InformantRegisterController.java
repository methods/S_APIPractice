package com.example.HearingsDemo.informantregister;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Informant Register viewstore", description = "Endpoints for querying the Informant Read-Model")
@RestController
@RequestMapping("/api/v1/informant-reporting")
public class InformantRegisterController {
    private final InformantRegisterService service;

    public InformantRegisterController(InformantRegisterService service) {
        this.service = service;
    }

    @Operation(
        summary = "Retrieve an informant record",
        description = "Fetches the full details of a specific informant register using its unique UUID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<InformantRegisterDTO> getRegisterById(
        @PathVariable UUID id
    ) {
        // 1. Call the service or throw Exception
        InformantRegisterDTO resultDto = service.getInformantResultById(id);

        return ResponseEntity.ok(resultDto);
    }
}