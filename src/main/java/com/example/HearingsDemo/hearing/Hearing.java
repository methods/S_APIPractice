package com.example.HearingsDemo.hearing;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "hearing")
@Getter
@Setter
@NoArgsConstructor
public class Hearing {

    // --- Composite Key ----
    @EmbeddedId
    private HearingId id;

    // --- Data ----
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "court_centre_name")
    private String courtCentreName;

    @Column(name = "court_code")
    private String courtCode;

    @Column(name = "judge_name")
    private  String judgeName;

    @Column(name = "prosecutor_name")
    private  String prosecutorName;

    @Column(name = "defence_name")
    private String defenceName;


}