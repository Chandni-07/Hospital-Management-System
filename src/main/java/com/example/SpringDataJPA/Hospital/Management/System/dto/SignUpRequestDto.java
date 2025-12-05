package com.example.SpringDataJPA.Hospital.Management.System.dto;


import com.example.SpringDataJPA.Hospital.Management.System.entities.type.RoleType;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpRequestDto {
    private String username;
    private String password;
    private String name;

    private Set<RoleType> roles = new HashSet<>();
}
