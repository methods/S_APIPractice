package com.example.HearingsDemo.hearing;

import com.example.HearingsDemo.person.Person;
import com.example.HearingsDemo.person.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class HearingService {

    private final HearingRepository hearingRepository;
    private final PersonRepository personRepository;

    public HearingService(HearingRepository hearingRepository, PersonRepository personRepository) {
        this.hearingRepository = hearingRepository;
        this.personRepository = personRepository;
    }

    // --- Single lookup with Application-Side Join (APL)---
    public HearingResponseDTO getHearingById(UUID hearingId) {

        // 1. Get the hearing rows
        List<Hearing> hearingRows = hearingRepository.findAllById_HearingUuid(hearingId);

        if (hearingRows.isEmpty()) {
            throw new ResourceNotFoundException("Hearing not found with ID: " + hearingId);
        }

        // 3. OPTIMIZED FETCH: Get the people associated with this hearing via PersonRepository partial key search
        List<Person> personEntities = personRepository.findAllById_HearingId(hearingId);

        // 4. (APL) Map everything with the helper function
        return mapToDTO(hearingRows, personEntities);

    }

    // --- Helper Mapper: Takes a List, returns a Single DTO
    private HearingResponseDTO mapToDTO(List<Hearing> hearingRows, List<Person> personEntities) {

        // 5. Get shared details: Deduplication
        Hearing firstRow = hearingRows.get(0);

        // 6. Turn personEntities into AttendeeDTOs
        List<AttendeeDTO> attendees = personEntities.stream()
            .map(p -> new AttendeeDTO(
                p.getId().getPersonUuid(),
                p.getFirstName(),
                p.getLastName()
            ))
            .toList();

        // 7. (APL) Maps final DTO fields with the correct values from hearingRow and created attendee list
        return new HearingResponseDTO(
            firstRow.getId().getHearingUuid(),
            firstRow.getStartDate(),
            firstRow.getCourtCentreName(),
            firstRow.getJudgeName(),
            attendees
        );
    }


}