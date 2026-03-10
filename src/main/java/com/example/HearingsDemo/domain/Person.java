package com.example.HearingsDemo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "person")
// Using @Getter/@Setter instead of @Data so Lombok does not generate an all-args constructor; JPA entities rely on a no-args constructor.
@Getter 
@Setter 
@NoArgsConstructor
public class Person {

    // --- Composite Key ----
    @EmbeddedId
    private PersonId id;

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
        if (!(o instanceof Person)) return false;

        // Step 3: Casting aka the "translation"
        Person person = (Person) o;

        // Step 4: The Business Logic (Identity Comparison)
        return id != null && Objects.equals(id, person.getId());
    }

    @Override
    public int hashCode() {
        // THE Equals and HashCode Contract:
        // We hash ONLY the fields used in equals() to keep them in sync
        return Objects.hash(id);
    }



}
