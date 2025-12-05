package com.example.SpringDataJPA.Hospital.Management.System.service;

import com.example.SpringDataJPA.Hospital.Management.System.dto.DoctorResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.OnBoardDoctorRequestDto;
import com.example.SpringDataJPA.Hospital.Management.System.entities.Doctor;
import com.example.SpringDataJPA.Hospital.Management.System.entities.User;
import com.example.SpringDataJPA.Hospital.Management.System.entities.type.RoleType;
import com.example.SpringDataJPA.Hospital.Management.System.repository.DoctorRepository;
import com.example.SpringDataJPA.Hospital.Management.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private  final UserRepository userRepository;

    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public DoctorResponseDto onBoardNewDoctor(OnBoardDoctorRequestDto onBoardDoctorRequestDto) {
        User user = userRepository.findById(onBoardDoctorRequestDto.getUserId()).orElseThrow();

        if(doctorRepository.existsById(onBoardDoctorRequestDto.getUserId())) {
            throw new IllegalArgumentException("Already a doctor");
        }

        Doctor doctor = Doctor.builder()
                .name(onBoardDoctorRequestDto.getName())
                .specialization(onBoardDoctorRequestDto.getSpecialization())
                .user(user)
                .build();

        user.getRoles().add(RoleType.DOCTOR);

        return modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
    }
}
