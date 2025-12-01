package com.example.SpringDataJPA.Hospital.Management.System.controller;

import com.example.SpringDataJPA.Hospital.Management.System.dto.AppointmentResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.CreateAppointmentRequestDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.PatientResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.service.AppointmentService;
import com.example.SpringDataJPA.Hospital.Management.System.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;


    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseDto> createNewAppointment(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createNewAppointment(createAppointmentRequestDto));
    }

    @GetMapping("/{id}")
    private ResponseEntity<PatientResponseDto> getPatientProfile(@PathVariable Long id) {
       // Long patientId = 4L;
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

}