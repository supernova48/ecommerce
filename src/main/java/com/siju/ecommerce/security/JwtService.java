package com.siju.ecommerce.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.siju.ecommerce.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(User user) {
    
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.expiration());


        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("role", user.getRole().name())
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(
                jwtProperties.secret());

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    private Claims extractAllClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(
            String token,
            User user) {

        String username = extractUsername(token);

        return username.equals(user.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(
            String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }



}
