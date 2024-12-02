package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_profile")
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer profileId;

    @Column(length = 50)
    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String phoneNumber;

    @Column(length = 50)
    private String email;

    private String address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(length = 16)
    private String noIdentitas;
}
