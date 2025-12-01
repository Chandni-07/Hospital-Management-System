package com.example.SpringDataJPA.Hospital.Management.System.service;

import com.example.SpringDataJPA.Hospital.Management.System.dto.PatientResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.entities.Patient;
import com.example.SpringDataJPA.Hospital.Management.System.repository.PatientRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

   /*@Transactional
    public Patient  getPatientById(Long id){

        Patient p1= patientRepository.findById(id).orElseThrow();
        Patient p2= patientRepository.findById(id).orElseThrow();


        System.out.println(p1 == p2);

        p1.setName("Yoyo");

//        patientRepository.save(p1);

        return p1;

    }*/

    public PatientResponseDto getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient Not " +
                "Found with id: " + patientId));
        return modelMapper.map(patient, PatientResponseDto.class);
    }

    public List<PatientResponseDto> getAllPatients(Integer pageNumber, Integer pageSize) {
        return patientRepository.findAllPatients(PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(patient -> modelMapper.map(patient, PatientResponseDto.class))
                .collect(Collectors.toList());
    }

}
