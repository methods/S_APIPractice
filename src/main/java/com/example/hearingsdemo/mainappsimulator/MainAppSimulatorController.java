package com.example.hearingsdemo.mainappsimulator; // Change to your actual package

/**
 * SIMULATOR ONLY: This simulates the external "Main Application" and Message Queue.
 * In production, the Verification API is strictly Read-Only.
 */

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/simulator/main-app")
@Profile("!prod") // <-- Lock for Enterprise safety
public class MainAppSimulatorController {

    private final JdbcTemplate jdbcTemplate;

    public MainAppSimulatorController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/defendants/{masterId}/gob-accounts")
    public String simulateCreateGobAccount(
        @PathVariable UUID masterId,
        @RequestBody Map<String, String> payload) throws InterruptedException {

        UUID hearingId = UUID.fromString(payload.get("hearingId"));
        UUID correlationId = UUID.randomUUID(); // The Main App generates this
        String accountNumber = payload.get("accountNumber");
        String caseRef = payload.get("caseReferences");

        String sql = """
            INSERT INTO defendant_gob_accounts 
            (master_defendant_id, hearing_id, account_correlation_id, account_number, account_request_time, case_references, created_time, updated_time) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
            masterId, hearingId, correlationId, accountNumber,
            LocalDateTime.now(), caseRef, LocalDateTime.now(), LocalDateTime.now());

        // SIMULATE THE MESSAGE QUEUE DELAY (1 second)
        Thread.sleep(1000);

        // Return the generated correlation ID so Postman can use it
        return correlationId.toString();

    }

    @PostMapping("/hearings/{hearingId}")
    public String simulateCreateHearing(
        @PathVariable UUID hearingId,
        @RequestBody Map<String, String> requestBody ) throws InterruptedException {

        LocalDate hearingDay = LocalDate.now();
        String payload = requestBody.get("payload");

        String sql = """
            INSERT INTO hearing_resulted_document
            (hearing_id, hearing_day, start_date, end_date, payload) VALUES (?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
            hearingId, hearingDay, LocalDate.now(), LocalDate.now(), payload
            );

        Thread.sleep(1000);

        return hearingId.toString();
    }
}