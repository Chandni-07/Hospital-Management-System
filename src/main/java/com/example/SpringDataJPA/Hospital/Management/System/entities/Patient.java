package com.example.SpringDataJPA.Hospital.Management.System.entities;

import com.example.SpringDataJPA.Hospital.Management.System.entities.type.BloodGroupType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Table (
        uniqueConstraints = {
             //   @UniqueConstraint(name = "unique_email", columnNames = {"email"}),
                @UniqueConstraint(name = "unique_name_and_dob", columnNames = {"name","birthDate"})
                }
)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String name;

    private LocalDate birthDate;

    @Column(unique = true,nullable = false)
    private String email;

    private String gender;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "patient_insurance_id") // owning side
    private Insurance insurance;


    @OneToMany(mappedBy = "patient",fetch = FetchType.EAGER, cascade=CascadeType.REMOVE,orphanRemoval = true) //inverse
    @ToString.Exclude
    private List<Appointment> appointments=new ArrayList<>();
}
