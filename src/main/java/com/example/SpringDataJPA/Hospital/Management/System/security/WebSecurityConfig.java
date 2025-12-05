package com.example.SpringDataJPA.Hospital.Management.System.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static com.example.SpringDataJPA.Hospital.Management.System.entities.type.PermissionType.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity
                .csrf( csrfConfig -> csrfConfig.disable()) // Disable CSRF for simplicity
                .sessionManagement( sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) // Use stateless sessions
                .authorizeHttpRequests(authorize -> authorize

                         .requestMatchers("/public/**",  "/auth/**").permitAll()
                       // .requestMatchers("/admin/**").hasRole("ADMIN")//only authenticated users can access admin endpoints
                        .requestMatchers("/doctors/**").hasAnyRole("DOCTOR","ADMIN")//only authenticated users can access doctor endpoints

                        .requestMatchers(HttpMethod.DELETE, "/admin/**")
                        .hasAnyAuthority(APPOINTMENT_DELETE.name(),
                                USER_MANAGE.name())
                        .anyRequest().authenticated() // all other requests need to be authenticated
                )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)


        .oauth2Login(oAuth2 -> oAuth2

                .failureHandler((HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)-> {
                        log.error("OAuth2 Authentication failed: {}", exception.getMessage());
                    handlerExceptionResolver.resolveException(request, response, null, exception);
                    }
                )
                .successHandler(oAuth2SuccessHandler)
        )
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
                        }));


              //  .formLogin(Customizer.withDefaults());
        return httpSecurity.build(); // Placeholder for actual security filter chain configuration
    }


}
