package com.example.hearingsdemo.ncesemailnotification;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NcesEmailNotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldReturnFullEmailNotificationFromDatabase() throws Exception {

        // Arrange
        UUID id = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        UUID masterDefendantId = UUID.randomUUID();
        String sendTo = "Example_person";
        String subject = "Test Subject note";

        String sql = """
        INSERT INTO nces_email_notification (
            id,
            material_id,
            notification_id,
            master_defendant_id,
            send_to,
            subject
        ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
            id,
            materialId,
            notificationId,
            masterDefendantId,
            sendTo,
            subject
        );

        mockMvc.perform(get("/api/v1/nces-notifications/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Identifiers
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.materialId").value(materialId.toString()))
            .andExpect(jsonPath("$.notificationId").value(notificationId.toString()))
            .andExpect(jsonPath("$.masterDefendantId").value(masterDefendantId.toString()))
            .andExpect(jsonPath("$.sendTo").value(sendTo))
            .andExpect(jsonPath("$.subject").value(subject));


    }
}