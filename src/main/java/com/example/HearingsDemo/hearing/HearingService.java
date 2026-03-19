package com.example.HearingsDemo.hearing;

import com.example.HearingsDemo.person.Person;
import com.example.HearingsDemo.person.PersonId;
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

    // --- Single lookup ---
    public HearingResponseDTO getHearingById(UUID hearingId) {

        // 1. Get the hearing rows
        List<Hearing> hearingRows = hearingRepository.findAllById_HearingUuid(hearingId);

        // Handle Not Found
        if (hearingRows.isEmpty()) {
            throw new ResourceNotFoundException("Hearing not found with ID: " + hearingId);
        }

        // 2. Create the list of PersonId objects from the hearing rows
        List<PersonId> personIds = hearingRows.stream()
            // each row that is found, create a new personId object using the unique personId and unique hearingId
            .map(h -> new PersonId(h.getId().getPersonUuid(), hearingId))
            .toList();

        // 3. Fetch the actual Person entities to get their names - that's why this takes in the composite key
        List<Person> personEntities = personRepository.findAllById(personIds);

        // 4. Map everything with the helper function
        return mapToDTO(hearingRows, personEntities);

    }

    // --- Helper Mapper: Takes a List, returns a Single DTO
    private HearingResponseDTO mapToDTO(List<Hearing> hearingRows, List<Person> personEntites) {

        // 5. Get shared details: Deduplication
        Hearing firstRow = hearingRows.get(0);

        // 6. Turn personEntities into AttendeeDTOs
        List<AttendeeDTO> attendees = personEntites.stream()
            .map(p -> new AttendeeDTO(
                p.getId().getPersonUuid(),
                p.getFirstName(),
                p.getLastName()
            ))
            .toList();

        // 7. Maps final DTO fields with the correct values from hearingRow and created attendee list
        return new HearingResponseDTO(
            firstRow.getId().getHearingUuid(),
            firstRow.getStartDate(),
            firstRow.getCourtCentreName(),
            firstRow.getJudgeName(),
            attendees
        );
    }


}