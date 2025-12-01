package com.example.SpringDataJPA.Hospital.Management.System.dto;


import lombok.Data;

@Data
public class DoctorResponseDto {

    private Long id;
    private String name;
    private String specialization;
    private String email;
}
