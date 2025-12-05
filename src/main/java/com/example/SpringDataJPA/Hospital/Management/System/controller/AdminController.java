package com.example.SpringDataJPA.Hospital.Management.System.controller;


import com.example.SpringDataJPA.Hospital.Management.System.dto.DoctorResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.OnBoardDoctorRequestDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.PatientResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.entities.User;
import com.example.SpringDataJPA.Hospital.Management.System.service.DoctorService;
import com.example.SpringDataJPA.Hospital.Management.System.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients(
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "10") Integer pageSize
    ) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(patientService.getAllPatients(pageNumber, pageSize));
    }

    @PostMapping("/onBoardNewDoctor")
    public ResponseEntity<DoctorResponseDto> onBoardNewDoctor(@RequestBody OnBoardDoctorRequestDto onboardDoctorRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.onBoardNewDoctor(onboardDoctorRequestDto));
    }

}