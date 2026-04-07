package com.example.hearingsdemo.defendantTrackingStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DefendantTrackingStatusIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldReturnFullTrackingStatusFromDatabase() throws Exception {

        // Arrange
        UUID offenceId = UUID.randomUUID();
        UUID defendantId = UUID.randomUUID();
        // Create the timestamps
        LocalDateTime now = LocalDateTime.now();

        String sql = "INSERT INTO defendant_tracking_status (offence_id, defendant_id, em_status, " +
            "em_last_modified_time, " +
            "woa_status, woa_last_modified_time) VALUES (?, ?, ?, ? , ? ,?)";

        jdbcTemplate.update(sql,
            offenceId,
            defendantId,
            true,   // em_status (Boolean)
            now,    // em_last_modified_time (datetime)
            false,  // woa_status (Boolean)
            now     // woa_last_modified_time (datetime)
        );

        // Act
        mockMvc.perform(get("/api/v1/offences/{offenceId}/tracking-status", offenceId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.offenceId").value(offenceId.toString()))
            .andExpect(jsonPath("$.defendantId").value(defendantId.toString()))
            .andExpect(jsonPath("$.emStatus").value(true))
            .andExpect(jsonPath("$.emLastModifiedTime").exists())
            .andExpect(jsonPath("$.woaStatus").value(false))
            .andExpect(jsonPath("$.woaLastModifiedTime").exists());
    }
}