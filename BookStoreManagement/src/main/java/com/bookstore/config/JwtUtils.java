package com.bookstore.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bookstore.model.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {

	public static String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(SecurityDetails.JWT_KEY.getBytes());
        return Jwts.builder()
                .setIssuer("YourAppName")
                .setSubject("JWT_Token")
                .claim("email", user.getEmail())
                .claim("authorities", "ROLE_"+ user.getRole()) // Adjust based on roles
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000)) // 1 day expiration
                .signWith(key).compact();
    }

   
}
