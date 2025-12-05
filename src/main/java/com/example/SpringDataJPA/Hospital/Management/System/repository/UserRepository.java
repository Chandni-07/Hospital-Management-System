package com.example.SpringDataJPA.Hospital.Management.System.repository;

import com.example.SpringDataJPA.Hospital.Management.System.entities.User;
import com.example.SpringDataJPA.Hospital.Management.System.entities.type.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByUsername(String username);

    Optional<User> findByProviderIdAndProviderType( String providerId,AuthProviderType providerType);
}