package com.example.SpringDataJPA.Hospital.Management.System.security;


import com.example.SpringDataJPA.Hospital.Management.System.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.DoubleStream;

@Component
public class AuthUtil {

    @Value("${jwt.secretkey}")
    private String jwtSecretKey;     //secrect key for jwt token

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)); //algorithm to create jwt token , header
    }


    public String generateAccessToken(User user) {    //user is the payload for jwt token


        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId",user.getId().toString())//payload
                .setIssuer("HospitalManagementSystem") //issuer
                .setIssuedAt(new Date()) //issued at
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) //expiration time 10 min
                .signWith(getSecretKey()) //signing the token with secret key
                .compact(); //compact the token
    }

    public String getUsernameFromToken(String token) {
        //will parse the token , valid or not and will parse the username

        Claims claims= Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();

    }
}
