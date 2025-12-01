package com.example.SpringDataJPA.Hospital.Management.System.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity
                .csrf( csrfConfig -> csrfConfig.disable()) // Disable CSRF for simplicity
                .sessionManagement( sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) // Use stateless sessions
                .authorizeHttpRequests(authorize -> authorize

                .requestMatchers("/public/**",  "/auth/**").permitAll()
           //     .requestMatchers("/admin/**").hasRole("ADMIN")//only authenticated users can access admin endpoints
                   //           .requestMatchers("/doctors/**").hasAnyRole("DOCTOR","ADMIN")//only authenticated users can access doctor endpoints
                                .anyRequest().authenticated() // all other requests need to be authenticated
                )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
              //  .formLogin(Customizer.withDefaults());
        return httpSecurity.build(); // Placeholder for actual security filter chain configuration
    }


}
