package com.example.hearingsdemo.informantregister;

import com.example.hearingsdemo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InformantRegisterServiceTest {

    @Mock
    private InformantRegisterRepository mockRepository;

    @InjectMocks
    private InformantRegisterService service;

    // ======================================================================
    // A simple utility method for creating test InformantRegister objects
    // ======================================================================
    private InformantRegister createEntity(UUID id) {
        return new InformantRegister(
            id,                                // id
            UUID.randomUUID(),                 // prosecutionAuthorityId
            "PA-CODE-123",                     // prosecutionAuthorityCode
            "OU-CODE-456",                     // prosecutionAuthorityOuCode
            LocalDate.now(),                   // registerDate
            UUID.randomUUID(),                 // fileId
            "{\"test\": \"payload\"}",         // payload
            "PENDING",                         // status
            LocalDateTime.now(),               // processedOn
            UUID.randomUUID(),                 // hearingId
            LocalDateTime.now(),               // registerTime
            LocalDateTime.now(),               // generatedTime
            LocalDate.now()                    // generatedDate
        );
    }

    @Test
    @DisplayName("getInformantResultById: Should return a fully mapped DTO when the record exists in the repository")
    void shouldReturnDto_whenRecordExists() {

        // Arrange
        UUID id = UUID.randomUUID();
        // Create fake entities to pass as mock for repository results
        InformantRegister entity = createEntity(id);

        when(mockRepository.findById(id)).thenReturn(Optional.of(entity));

        //Act
        InformantRegisterDTO result = service.getInformantResultById(id);

        //Assert
        assertThat(result).isNotNull();

        // Identifiers
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.prosecutionAuthorityId()).isEqualTo(entity.getProsecutionAuthorityId());
        assertThat(result.fileId()).isEqualTo(entity.getFileId());
        assertThat(result.hearingId()).isEqualTo(entity.getHearingId());

        // Authority Codes
        assertThat(result.prosecutionAuthorityCode()).isEqualTo("PA-CODE-123");
        assertThat(result.prosecutionAuthorityOuCode()).isEqualTo("OU-CODE-456");

        // Status & Payload
        assertThat(result.status()).isEqualTo("PENDING");
        assertThat(result.payload()).isEqualTo("{\"test\": \"payload\"}");

        // Dates (LocalDate)
        assertThat(result.registerDate()).isEqualTo(entity.getRegisterDate());
        assertThat(result.generatedDate()).isEqualTo(entity.getGeneratedDate());

        // Timestamps (LocalDateTime)
        assertThat(result.processedOn()).isEqualTo(entity.getProcessedOn());
        assertThat(result.registerTime()).isEqualTo(entity.getRegisterTime());
        assertThat(result.generatedTime()).isEqualTo(entity.getGeneratedTime());


    }

    @Test
    @DisplayName("getInformantResultById: Should throw ResourceNotFoundException when the requested ID is missing")
    void shouldThrowResourceNotFoundException_whenRecordDoesNotExist() {

        // Arrange
        UUID id = UUID.randomUUID();
        when(mockRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getInformantResultById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Informant result not found for id: " + id);

    }
}