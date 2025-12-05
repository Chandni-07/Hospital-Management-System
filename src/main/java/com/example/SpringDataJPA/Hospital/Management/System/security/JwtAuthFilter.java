package com.example.SpringDataJPA.Hospital.Management.System.security;

import com.example.SpringDataJPA.Hospital.Management.System.entities.User;
import com.example.SpringDataJPA.Hospital.Management.System.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter  extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            log.info("incoming request: {} ", request.getRequestURI());


            final String requestTokenHeader = request.getHeader("Authorization");
            //"bearer jgjugkjhlhlj;lk;ikohjfhfj"

            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
                log.warn("Invalid Token, not start with bearer string");
                filterChain.doFilter(request, response); // proceed further within filter chain
                return;

            }

            String token = requestTokenHeader.split("Bearer ")[1];
            //  "Bearer " "wkeuhwkeifowijfeowejforwurjowejdlwsjedowehfoef"

            log.info("Token extracted: {} ", token);

            String username = authUtil.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Username extracted from token : {} ", username);

                User user = userRepository.findByUsername(username).orElseThrow();
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                log.info("User authenticated successfully: {} ", username);
            }
                filterChain.doFilter(request, response);
            }
        catch(Exception e){
                log.error("Error during JWT processing: {}", e.getMessage());
                handlerExceptionResolver.resolveException(request, response, null, e);
            }
        }
    }


