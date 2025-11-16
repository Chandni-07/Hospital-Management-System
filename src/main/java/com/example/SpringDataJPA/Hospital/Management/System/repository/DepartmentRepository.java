package com.example.SpringDataJPA.Hospital.Management.System.repository;

import com.example.SpringDataJPA.Hospital.Management.System.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}