package com.example.SpringDataJPA.Hospital.Management.System.service;

import com.example.SpringDataJPA.Hospital.Management.System.entities.Insurance;
import com.example.SpringDataJPA.Hospital.Management.System.entities.Patient;
import com.example.SpringDataJPA.Hospital.Management.System.repository.InsuranceRepository;
import com.example.SpringDataJPA.Hospital.Management.System.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final PatientRepository patientRepository;
    private final InsuranceRepository insuranceRepository;

    @Transactional
    public Patient assignInsuranceToPatient(Long patientId, Insurance insurance) {
        Patient patient = patientRepository.findById(patientId).orElseThrow();
        patient.setInsurance(insurance);
        insurance.setPatient(patient); // set the bidirectional relationship

        return patient;


    }

    @Transactional
    public Patient disassociateInsuranceFromPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow();
        Insurance insurance = patient.getInsurance();
        if (insurance != null) {
            patient.setInsurance(null);
        }
      return
        patient;

    }

}

