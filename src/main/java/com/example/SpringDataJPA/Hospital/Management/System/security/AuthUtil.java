package com.example.SpringDataJPA.Hospital.Management.System.security;


import com.example.SpringDataJPA.Hospital.Management.System.entities.User;
import com.example.SpringDataJPA.Hospital.Management.System.entities.type.AuthProviderType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.DoubleStream;

@Component
@Slf4j
public class AuthUtil {

    @Value("${jwt.secretkey}")
    private String jwtSecretKey;     //secrect key for jwt token

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)); //algorithm to create jwt token , header
    }


    public String generateAccessToken(User user) {    //user is the payload for jwt token


        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId().toString())//payload
                .setIssuer("HospitalManagementSystem") //issuer
                .setIssuedAt(new Date()) //issued at
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) //expiration time 10 min
                .signWith(getSecretKey()) //signing the token with secret key
                .compact(); //compact the token
    }

    public String getUsernameFromToken(String token) {
        //will parse the token , valid or not and will parse the username

        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();

    }

    public AuthProviderType getProviderTypeFromRegistrationId(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProviderType.GOOGLE;
            case "github" -> AuthProviderType.GITHUB;
            case "facebook" -> AuthProviderType.FACEBOOK;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
        };
    }

    public String determineProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId) {
        String providerId = switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("id").toString();

            default -> {
                log.error("Unsupported OAuth2 provider: {}", registrationId);
                throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
            }
        };

        if (providerId == null || providerId.isBlank()) {
            log.error("Unable to determine providerId for provider: {}", registrationId);
            throw new IllegalArgumentException("Unable to determine providerId for OAuth2 login");
        }
        return providerId;
    }

    public String determineUsernameFromOAuth2User(OAuth2User oAuth2User, String registrationId, String providerId) {
        String email = oAuth2User.getAttribute("email");
        if (email != null && !email.isBlank()) {
            return email;
        }
        return switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("login");
            default -> providerId;
        };
    }
}
