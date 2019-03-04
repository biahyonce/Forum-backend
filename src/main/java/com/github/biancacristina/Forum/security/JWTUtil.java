package com.github.biancacristina.Forum.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {
        // Generate the token

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    public boolean validToken(String token) {

        // Claim stores information about the token
        Claims claims = getClaims(token);

        if (claims != null) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());

            if (username != null && expirationDate != null && now.before(expirationDate)) {
                return true;
            }
        }

        return false;
    }

    public String getUsername(String token) {
        // Claim armazena as reinvidicacoes do token
        Claims claims = getClaims(token);

        if (claims != null) {
            return claims.getSubject();
        }

        return null;
    }

    private Claims getClaims(String token) {
        // Returns claims using a token

        try {
            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        }

        catch (Exception e) {
            return null;
        }
    }

}
