package com.example.hearingsdemo.informantregister;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InformantRegisterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldReturnFullInformantRegisterFromDatabase() throws Exception {

        // Arrange // Define your test data variables
        UUID id = UUID.randomUUID();
        UUID prosecutionAuthorityId = UUID.randomUUID();
        UUID fileId = UUID.randomUUID();
        UUID hearingId = UUID.randomUUID();
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        String payload = "{\"data\": \"sample payload\"}";
        String status = "PROCESSED";

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
            id,                               // id (UUID)
            prosecutionAuthorityId,           // prosecution_authority_id (UUID)
            "PA-CODE-001",                    // prosecution_authority_code (TEXT)
            "OU-CODE-99",                     // prosecution_authority_ou_code (TEXT)
            today,                            // register_date (date)
            fileId,                           // file_id (UUID)
            payload,                          // payload (TEXT)
            status,                           // status (TEXT)
            now,                              // processed_on (datetime)
            hearingId,                        // hearing_id (UUID)
            now,                              // register_time (datetime)
            now,                              // generated_time (datetime)
            today                             // generated_date (date)
        );

        // Act & Assert
        mockMvc.perform(get("/api/v1/informant-reporting/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Identifiers
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.prosecutionAuthorityId").exists())
            .andExpect(jsonPath("$.fileId").exists())
            .andExpect(jsonPath("$.hearingId").exists())
            // Strings & Codes
            .andExpect(jsonPath("$.prosecutionAuthorityCode").value("PA-CODE-001"))
            .andExpect(jsonPath("$.prosecutionAuthorityOuCode").value("OU-CODE-99"))
            .andExpect(jsonPath("$.status").value("PROCESSED"))
            .andExpect(jsonPath("$.payload").value("{\"data\": \"sample payload\"}"))

            // Date Fields
            .andExpect(jsonPath("$.registerDate").value(today.toString()))
            .andExpect(jsonPath("$.generatedDate").value(today.toString()))

            // DateTime Fields
            // Note: Use .startsWith() if your JSON includes milliseconds you didn't mock
            // Match timestamps up to the second (index 19) to avoid
            // Nano/Millisecond precision mismatches between Java, Postgres, and Jackson.
            .andExpect(jsonPath("$.processedOn").value(startsWith(now.toString().substring(0, 19))))
            .andExpect(jsonPath("$.registerTime").value(startsWith(now.toString().substring(0, 19))))
            .andExpect(jsonPath("$.generatedTime").value(startsWith(now.toString().substring(0, 19))));

    }
}