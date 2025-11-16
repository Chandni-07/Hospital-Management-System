package com.example.SpringDataJPA.Hospital.Management.System.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String specialization;

    @Column(unique = true, length = 100)
    private String email;

    @ManyToMany(mappedBy = "doctors")
    @ToString.Exclude
    private Set<Department> departments = new HashSet<>();


    @OneToMany(mappedBy = "doctor",fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Appointment> appointments = new ArrayList<>();



}
