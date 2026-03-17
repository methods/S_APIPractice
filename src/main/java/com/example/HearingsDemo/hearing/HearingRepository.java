package com.example.HearingsDemo.hearing;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HearingRepository extends JpaRepository<Hearing, HearingId> {

    List<Hearing> findAllById_HearingUuid(UUID hearingId);
}