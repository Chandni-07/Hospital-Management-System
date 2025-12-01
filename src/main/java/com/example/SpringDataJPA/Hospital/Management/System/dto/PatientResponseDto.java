package com.example.SpringDataJPA.Hospital.Management.System.dto;


import com.example.SpringDataJPA.Hospital.Management.System.entities.type.BloodGroupType;
import lombok.Data;

import java.time.LocalDate;

@Data

public class PatientResponseDto {
    private Long id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private BloodGroupType bloodGroup;
}
