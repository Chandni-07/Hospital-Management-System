package com.example.SpringDataJPA.Hospital.Management.System.service;


import com.example.SpringDataJPA.Hospital.Management.System.entities.Appointment;
import com.example.SpringDataJPA.Hospital.Management.System.entities.Doctor;
import com.example.SpringDataJPA.Hospital.Management.System.entities.Patient;
import com.example.SpringDataJPA.Hospital.Management.System.repository.AppointmentRepository;
import com.example.SpringDataJPA.Hospital.Management.System.repository.DoctorRepository;
import com.example.SpringDataJPA.Hospital.Management.System.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentService {


    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;

    public Appointment createNewAppointment(Long doctorId, Long patientId, Appointment appointment) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        Patient patient = patientRepository.findById(patientId).orElseThrow();

        if (appointment.getId() != null) {
            throw new IllegalArgumentException("Appointment ID must be null for new appointments");
        }
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        patient.getAppointments().add(appointment);
        doctor.getAppointments().add(appointment);// bi-directional relationship


        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment reassignAppointmentToAnotherDoctor(Long appointmentId, Long newDoctorId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        Doctor newDoctor = doctorRepository.findById(newDoctorId).orElseThrow();

        appointment.setDoctor(newDoctor); // automatically call update because of transactional context


        newDoctor.getAppointments().add(appointment); // just for bidirectional consistency

        return appointment;


    }

}
