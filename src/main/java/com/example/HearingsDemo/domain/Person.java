package com.example.HearingsDemo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "person", schema = "public") // <-- table called public, schema is "public"
@IdClass(PersonId.class) // <--- referencing Composite Key
@Getter // Replaces @Data
@Setter // replaces @Data - stops us suing AllArgsConstructor (?)
@NoArgsConstructor
public class Person {

    // --- Composite Key Fields ----
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Id 
    @Column(name = "hearing_id", nullable = false)
    private UUID hearingId;

    // --- Data ----
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "address_3")
    private String address3;

    @Column(name = "address_4")
    private String address4;

    @Column(name = "post_code")
    private String postCode;

    // --- Manual Equals/HashCode
    @Override // overrides default settings of method with matching name
    public boolean equals(Object o) {

        // Step 1: performance optimization with reference check
        if (this == o) return true;

        // Step 2: Safety and Type Check
        // If the other object is null OR not created from the exact same runtime class, return false.
        // Using getClass() (instead of instanceof) enforces strict equality by class,
        // which is generally safer for JPA/Hibernate entities to avoid inheritance/proxy-related issues.
        if (o == null || getClass() != o.getClass()) return false; 

        // Step 3: Casting aka the "translation"
        // Cast 'o' from a genric Object to object/class 'Person'
        // This allows us to access the private fields (.id, .hearingId)
        Person person = (Person) o;

        // Step 4: The Business Logic (Identity Comparison)
        // We compare the Composite Keys using Object.equals() to safely handle cases where an ID might be null (avoids crashes).
        // compares id and hearingId values for equality (null-safe) between this and the other Person
        // BOTH 'id' and 'hearingId' must match for this to be the same person
        return Objects.equals(id, person.id) && Objects.equals(hearingId, person.hearingId);
    }

    @Override
    public int hashCode() {
        // THE Equals and HashCode Contract:
        // We hash ONLY the fields used in equals() to keep them in sync
        return Objects.hash(id, hearingId);
    }



}
