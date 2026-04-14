package com.example.hearingsdemo.mainappsimulator;

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
@Profile("simulator") // <-- Lock for Enterprise safety
public class MainAppSimulatorController {

    private final JdbcTemplate jdbcTemplate;

    public MainAppSimulatorController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // =============================================================================
    // Defendant_GOB_Accounts
    // =============================================================================
    @PostMapping("/defendants/{masterId}/gob-accounts")
    public String simulateCreateGobAccount(
        @PathVariable UUID masterId,
        @RequestBody Map<String, String> payload) {

        UUID hearingId = getRequiredUUID(payload, "hearingId");
        String caseRef = getRequiredString(payload, "caseReferences");
        String accountNumber = payload.get("accountNumber");
        UUID correlationId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now(java.time.ZoneOffset.UTC);


        String sql = """
            INSERT INTO defendant_gob_accounts 
            (master_defendant_id, hearing_id, account_correlation_id, account_number, account_request_time, case_references, created_time, updated_time) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
            masterId,
            hearingId,
            correlationId,
            accountNumber,
            now,           // account_request_time
            caseRef,
            now,           // created_time
            now            // updated_time
        );

        // SIMULATE THE MESSAGE QUEUE DELAY (1 second)
        simulateDelay(1000);

        // Return the generated correlation ID so Postman can use it
        return correlationId.toString();

    }

    // =============================================================================
    // Hearing_Resulted_Document
    // =============================================================================
    @PostMapping("/hearings/{hearingId}")
    public String simulateCreateHearing(
        @PathVariable UUID hearingId,
        @RequestBody Map<String, String> requestBody ) {

        LocalDate hearingDay = LocalDate.now(java.time.ZoneOffset.UTC);
        String payload = requestBody.get("payload");

        String sql = """
            INSERT INTO hearing_resulted_document
            (hearing_id, hearing_day, start_date, end_date, payload) VALUES (?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
            hearingId,
            hearingDay,
            LocalDate.now(java.time.ZoneOffset.UTC),
            LocalDate.now(java.time.ZoneOffset.UTC),
            payload
        );

        simulateDelay(1000);

        return hearingId.toString();
    }

    // =============================================================================
    // Defendant_Tracking_Status
    // =============================================================================
    @PostMapping("/offences/{offenceId}/tracking-status")
    public String simulateCreateOffenceTrackingStatus(
        @PathVariable UUID offenceId,
        @RequestBody Map<String, String> requestBody ) {

        UUID defendantId = getRequiredUUID(requestBody, "defendantId");
        Boolean emStatus = Boolean.parseBoolean(requestBody.get("emStatus"));
        Boolean woaStatus = Boolean.parseBoolean(requestBody.get("woaStatus"));
        LocalDateTime now = LocalDateTime.now(java.time.ZoneOffset.UTC);


        String sql = """
            INSERT INTO defendant_tracking_status (
            offence_id, defendant_id, em_status, em_last_modified_time, woa_status, woa_last_modified_time) VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sql,
            offenceId,
            defendantId,
            emStatus,
            now,            // em_last_modified_time
            woaStatus,
            now             // woa_last_modified_time
        );

        simulateDelay(1000);

        return offenceId.toString();
    }

    // =============================================================================
    // NCES_Email_Notification
    // =============================================================================
    @PostMapping("/nces-notifications/{id}")
    public String simulateEmailNotification(
        @PathVariable UUID id,
        @RequestBody Map<String, String> requestBody
    ) {

        // Extract IDs from Postman body
        UUID materialId = getOptionalUUID(requestBody, "materialId");
        UUID notificationId = getOptionalUUID(requestBody, "notificationId");
        UUID masterId = getOptionalUUID(requestBody, "masterDefendantId");

        String sendTo = requestBody.get("sendTo");
        String subject = requestBody.get("subject");

        String sql = """
            INSERT INTO nces_email_notification (
            id, material_id, notification_id, master_defendant_id, send_to, subject
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
            id, materialId, notificationId, masterId, sendTo, subject
            );

        // Mimic Async MQ Latency
        simulateDelay(2000);

        return id.toString();

    }

    // =============================================================================
    // Informant_Register
    // =============================================================================
    @PostMapping("/informant-reporting/{id}")
    public String simulateCreateInformant(
        @PathVariable UUID id,
        @RequestBody Map<String, String> requestBody
    ) {
        // 1. Validate Required (Liquibase NOT NULL)
        UUID authId = getRequiredUUID(requestBody, "prosecutionAuthorityId");
        String payload = getRequiredString(requestBody, "payload");

        // 2. Validate Optional (Liquibase NULL)
        UUID fileId = getOptionalUUID(requestBody, "fileId");
        UUID hearingId = getOptionalUUID(requestBody, "hearingId");

        String authCode = requestBody.get("prosecutionAuthorityCode");
        String authOuCode = requestBody.get("prosecutionAuthorityOuCode");
        String status = requestBody.get("status");

        LocalDate todayDate = LocalDate.now(java.time.ZoneOffset.UTC);
        LocalDateTime nowTime = LocalDateTime.now(java.time.ZoneOffset.UTC);

        String sql = """
        INSERT INTO informant_register (
            id,
            prosecution_authority_id,
            prosecution_authority_code,
            prosecution_authority_ou_code,
            register_date,
            file_id,
            payload,
            status,
            processed_on,
            hearing_id,
            register_time,
            generated_time,
            generated_date
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
            id,               // From PathVariable
            authId,           // From JSON
            authCode,         // From JSON
            authOuCode,       // From JSON
            todayDate,        // Generated
            fileId,           // From JSON
            payload,          // From JSON
            status,           // From JSON
            nowTime,          // Generated (Processed On)
            hearingId,        // From JSON
            nowTime,          // Generated (Register Time)
            nowTime,          // Generated (Generated Time)
            todayDate         // Generated (Generated Date)
        );

        // Mimic Async MQ Latency
        simulateDelay(2000);

        return id.toString();

    }

    // =============================================================================
    // PRIVATE HELPERS
    // =============================================================================

    /** Ensures a string is present and not blank. Returns 400 if missing. */
    private String getRequiredString(Map<String, String> payload, String key) {
        String value = payload.get(key);
        if (value == null || value.isBlank()) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, "Missing required field: " + key);
        }
        return value;
    }

    /** Ensures a UUID is present and valid. Returns 400 if missing/invalid. */
    private UUID getRequiredUUID(Map<String, String> payload, String key) {
        String value = getRequiredString(payload, key);
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, "Field '" + key + "' must be a valid UUID.");
        }
    }

    /** Returns a UUID if present, or null if missing. Returns 400 if format is invalid. */
    private UUID getOptionalUUID(Map<String, String> payload, String key) {
        String value = payload.get(key);
        if (value == null || value.isBlank()) return null;
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, "Field '" + key + "' must be a valid UUID.");
        }
    }

    // Properly handles Thread.sleep and satisfies the Java Compiler/Linter.
    private void simulateDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // Restores the interrupted status
            Thread.currentThread().interrupt();
        }
    }

}