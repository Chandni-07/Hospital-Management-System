package com.example.SpringDataJPA.Hospital.Management.System.security;


import com.example.SpringDataJPA.Hospital.Management.System.dto.LoginRequestDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.LoginResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.SignUpRequestDto;
import com.example.SpringDataJPA.Hospital.Management.System.dto.SignupResponseDto;
import com.example.SpringDataJPA.Hospital.Management.System.entities.Patient;
import com.example.SpringDataJPA.Hospital.Management.System.entities.User;
import com.example.SpringDataJPA.Hospital.Management.System.entities.type.AuthProviderType;
import com.example.SpringDataJPA.Hospital.Management.System.entities.type.RoleType;
import com.example.SpringDataJPA.Hospital.Management.System.repository.PatientRepository;
import com.example.SpringDataJPA.Hospital.Management.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        ); // if user has already sighned up and is there in the database


        User user = (User) authentication.getPrincipal();


        // Here you can generate a JWT token or any other token as per your requirement

        String token = authUtil.generateAccessToken(user);
        return new LoginResponseDto(token, user.getId());


    }

    // this code was used by JWT based authentication signup
   /* public SignupResponseDto signup(LoginRequestDto signupRequestDto) {

        User user=userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);
        if(user!=null){
            throw new RuntimeException("User already exists with username: " + signupRequestDto.getUsername());
        }
        user=userRepository.save(User.builder()
                        .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build());
        return new SignupResponseDto(user.getId(), user.getUsername());

    }*/

    // login controller
    public SignupResponseDto signup(SignUpRequestDto signupRequestDto) {
        User user = signUpInternal(signupRequestDto, AuthProviderType.EMAIL, null);
        return new SignupResponseDto(user.getId(), user.getUsername());
    }

    private User signUpInternal(SignUpRequestDto signupRequestDto, AuthProviderType authProviderType, String providerId) {
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if (user != null) throw new IllegalArgumentException("User already exists");

        user = User.builder()
                .username(signupRequestDto.getUsername())
                .providerId(providerId)
                .providerType(authProviderType)
                .roles(signupRequestDto.getRoles())    // every signed user will be stored as patient role
                .build();

        if(authProviderType == AuthProviderType.EMAIL) {
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }
        userRepository.save(user);
        Patient patient = Patient.builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getUsername())
                .user(user)
                .build();

        patientRepository.save(patient);


        return user;


    }

    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);
        String email = oAuth2User.getAttribute("email");
        String name= oAuth2User.getAttribute("name");
        User emailUser = userRepository.findByUsername(email).orElse(null);

        if (user == null && emailUser == null) {
            // signup flow:
            String username = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
            user = signUpInternal(new SignUpRequestDto(username,null, name, Set.of(RoleType.PATIENT)), providerType, providerId);
        } else if (user != null) {
            if (email != null && !email.isBlank() && !email.equals(user.getUsername())) {
                user.setUsername(email);
                userRepository.save(user);
            }
        } else {
            throw new BadCredentialsException("This email is already registered with provider " + emailUser.getProviderType());
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto(authUtil.generateAccessToken(user), user.getId());
        return ResponseEntity.ok(loginResponseDto);

    }
}
