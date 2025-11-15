package com.example.SpringDataJPA.Hospital.Management.System.service;

import com.example.SpringDataJPA.Hospital.Management.System.entities.Patient;
import com.example.SpringDataJPA.Hospital.Management.System.repository.PatientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

   @Transactional
    public Patient  getPatientById(Long id){

        Patient p1= patientRepository.findById(id).orElseThrow();
        Patient p2= patientRepository.findById(id).orElseThrow();


        System.out.println(p1 == p2);

        p1.setName("Yoyo");

//        patientRepository.save(p1);

        return p1;

    }

}
