package com.example.HearingsDemo.defendantgobaccounts;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Defendant GOB Accounts Viewstore", description = "Query Endpoints for fetching associated accounts")
@RestController
@RequestMapping("/api/v1/defendant-gob-accounts")
public class DefendantGOBAccountsController {

    private final DefendantGOBAccountsService service;

    public DefendantGOBAccountsController(DefendantGOBAccountsService service) {
        this.service = service;
    }

    @Operation(summary = "Get associated account details", description = "Retrieves account by IDs")
    @GetMapping("/{masterDefendantId}/correlations/{accountCorrelationId}")
    public ResponseEntity<DefendantGOBAccountDTO> getAccountById(
        @PathVariable UUID masterDefendantId,
        @PathVariable UUID accountCorrelationId
    ) {
        DefendantGOBAccountDTO accountDto = service.getAccountByIds(masterDefendantId, accountCorrelationId);

        return ResponseEntity.ok(accountDto);
    }
}