package com.bookstore.config;

import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bookstore.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

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
	
	
	static public String decodeJwt(String jwt) {
//		String jwt= request.getHeader(SecurityDetails.JWT_HEADER);
//		System.out.println(jwt);
		String email="";
		if(jwt != null && jwt.startsWith("Bearer")) {

			try {
				String token  = jwt.substring(7).trim();
				SecretKey key= Keys.hmacShaKeyFor(SecurityDetails.JWT_KEY.getBytes());
				Claims claims= Jwts.parserBuilder().setSigningKey(key).build().
				parseClaimsJws(token).getBody();
				String username= String.valueOf(claims.get("email"));
				email=username;
			} catch (Exception e) {
				System.out.println("Invalid token inside jw");
				System.out.println(e);
			}
	   }
		return email;
	}


   
}
