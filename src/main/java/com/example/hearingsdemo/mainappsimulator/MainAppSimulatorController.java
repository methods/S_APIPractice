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

    // =============================================================================
    // Defendant_GOB_Accounts
    // =============================================================================
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

    // =============================================================================
    // Hearing_Resulted_Document
    // =============================================================================
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

    // =============================================================================
    // Defendant_Tracking_Status
    // =============================================================================
    @PostMapping("/offences/{offenceId}/tracking-status")
    public String simulateCreateOffenceTrackingStatus(
        @PathVariable UUID offenceId,
        @RequestBody Map<String, String> requestBody ) throws InterruptedException {

        UUID defendantId = UUID.fromString(requestBody.get("defendantId"));
        Boolean emStatus = Boolean.parseBoolean(requestBody.get("emStatus"));
        LocalDateTime emLastModifiedTime = LocalDateTime.now();
        Boolean woaStatus = Boolean.parseBoolean(requestBody.get("woaStatus"));
        LocalDateTime woaLastModifiedTime = LocalDateTime.now();

        String sql = """
            INSERT INTO defendant_tracking_status (
            offence_id, defendant_id, em_status, em_last_modified_time, woa_status, woa_last_modified_time) VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sql,
            offenceId, defendantId, emStatus, emLastModifiedTime, woaStatus, woaLastModifiedTime);

        Thread.sleep(1000);

        return offenceId.toString();
    }

    // =============================================================================
    // NCES_Email_Notification
    // =============================================================================
    @PostMapping("/nces-notifications/{id}")
    public String simulateEmailNotification(
        @PathVariable UUID id,
        @RequestBody Map<String, String> requestBody
    ) throws InterruptedException {

        // Extract IDs from Postman body
        UUID materialId = requestBody.get("materialId") != null ? UUID.fromString(requestBody.get("materialId")) : null;
        UUID notificationId = requestBody.get("notificationId") != null ? UUID.fromString(requestBody.get(
            "notificationId")) : null;
        UUID masterId = requestBody.get("masterDefendantId") != null ? UUID.fromString(requestBody.get(
            "masterDefendantId")) : null;

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
        Thread.sleep(2000);

        return id.toString();

    }

    // =============================================================================
    // Informant_Register
    // =============================================================================
    @PostMapping("/informant-reporting/{id}")
    public String simulateCreateInformant(
        @PathVariable UUID id,
        @RequestBody Map<String, String> requestBody
    ) throws InterruptedException {

        String authCode = requestBody.get("prosecutionAuthorityCode");
        String authOuCode = requestBody.get("prosecutionAuthorityOuCode");
        String payload = requestBody.get("payload");
        String status = requestBody.get("status");

        // Extract IDs from Postman Body
        UUID authId = UUID.fromString(requestBody.get("prosecutionAuthorityId"));
        UUID fileId = requestBody.get("fileId") != null ? UUID.fromString(requestBody.get("fileId")) : null;
        UUID hearingId = requestBody.get("hearingId") != null ? UUID.fromString(requestBody.get("hearingId")) : null;

        LocalDate todayDate = LocalDate.now();
        LocalDateTime nowTime = LocalDateTime.now();

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
        Thread.sleep(2000);

        return id.toString();

    }

}