package com.example.SpringDataJPA.Hospital.Management.System.repository;

import com.example.SpringDataJPA.Hospital.Management.System.entities.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}