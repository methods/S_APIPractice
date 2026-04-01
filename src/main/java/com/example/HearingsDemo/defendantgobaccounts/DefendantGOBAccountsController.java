package com.example.HearingsDemo.defendantgobaccounts;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Operation(summary = "Get all associated account accounts", description = "Retrieves by masterDefendantId and " +
        "hearingId")
    @GetMapping
    public ResponseEntity<List<DefendantGOBAccountDTO>> getAccounts(
        @RequestParam UUID masterDefendantId,
        @RequestParam UUID hearingId
    ) {
        List<DefendantGOBAccountDTO> accounts = service.getAllAccountsForHearing(masterDefendantId, hearingId);

        return ResponseEntity.ok(accounts);
    }
}