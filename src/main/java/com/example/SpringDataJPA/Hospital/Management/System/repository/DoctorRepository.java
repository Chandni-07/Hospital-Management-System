package com.example.SpringDataJPA.Hospital.Management.System.repository;

import com.example.SpringDataJPA.Hospital.Management.System.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}