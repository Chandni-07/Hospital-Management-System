package com.example.SpringDataJPA.Hospital.Management.System.security;


import com.example.SpringDataJPA.Hospital.Management.System.dto.LoginRequestDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.LoginResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.SignupResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.entities.User;
import com.example.SpringDataJPA.Hospital.Management.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        ); // if user has already sighned up and is there in the database


        User user=(User)authentication.getPrincipal();


        // Here you can generate a JWT token or any other token as per your requirement

        String token = authUtil.generateAccessToken(user);
        return new LoginResponseDto(token,user.getId());


    }

    public SignupResponseDto signup(LoginRequestDto signupRequestDto) {

        User user=userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);
        if(user!=null){
            throw new RuntimeException("User already exists with username: " + signupRequestDto.getUsername());
        }
        user=userRepository.save(User.builder()
                        .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build());
        return new SignupResponseDto(user.getId(), user.getUsername());

    }
}
