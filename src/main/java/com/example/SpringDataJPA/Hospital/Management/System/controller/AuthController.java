package com.example.SpringDataJPA.Hospital.Management.System.controller;


import com.example.SpringDataJPA.Hospital.Management.System.dto.LoginRequestDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.LoginResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.SignUpRequestDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.SignupResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


   private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
          return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(SignUpRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

}
