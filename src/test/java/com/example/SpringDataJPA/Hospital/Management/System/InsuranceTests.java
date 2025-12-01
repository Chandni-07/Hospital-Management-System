package com.example.SpringDataJPA.Hospital.Management.System;


import com.example.SpringDataJPA.Hospital.Management.System.entities.Appointment;
import com.example.SpringDataJPA.Hospital.Management.System.entities.Insurance;
import com.example.SpringDataJPA.Hospital.Management.System.entities.Patient;
import com.example.SpringDataJPA.Hospital.Management.System.service.AppointmentService;
import com.example.SpringDataJPA.Hospital.Management.System.service.InsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class InsuranceTests {


    @Autowired
    private InsuranceService insuranceService;


    @Autowired
    private AppointmentService appointmentService;

    @Test
    public void testInsuranceFunctionality() {

        Insurance insurance = Insurance.builder().policyNumber("HDFC1234").provider("HDFC")
                .validUntil(LocalDate.of(2030, 12, 31)).build();


        Patient patient = insuranceService.assignInsuranceToPatient(1L, insurance);
        System.out.println(patient);
        // Placeholder for insurance-related tests



        Patient updatedPatient = insuranceService.disassociateInsuranceFromPatient(1L);
        System.out.println(updatedPatient);

    }

    /*@Test
    public void testCreateAppointment() {
        Appointment appointment = Appointment.builder()
                .appointmentTime(LocalDateTime.of(2025, 11, 1, 14, 0, 0))
                .reason("Cancer")
                .build();


       Appointment newApp= appointmentService.createNewAppointment(1l,2l,appointment);
        System.out.println(newApp);

        Appointment updatedApp= appointmentService.reassignAppointmentToAnotherDoctor(newApp.getId(),3L);
        System.out.println(updatedApp);
    }*/
}