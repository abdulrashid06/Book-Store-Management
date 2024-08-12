package com.bookstore.config;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
	        SecretKey key = Keys.hmacShaKeyFor(SecurityDetails.JWT_KEY.getBytes());
	        String jwt = Jwts.builder()
	                .setIssuer("YourAppName")
	                .setSubject("JWT_Token")
	                .claim("email", authentication.getName())
	                .claim("authorities", authentication.getAuthorities().stream()
	                        .map(GrantedAuthority::getAuthority)
	                        .collect(Collectors.joining(",")))
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(new Date().getTime() + 86400000)) // 1 day expiration
	                .signWith(key).compact();

	        response.setHeader(SecurityDetails.JWT_HEADER, "Bearer " + jwt);
	    }

	    filterChain.doFilter(request, response);
	}


//    private String getValue(Collection<? extends GrantedAuthority> collection) {
//        Set<String> set = new HashSet<>();
//        for (GrantedAuthority autho : collection) {
//            set.add(autho.getAuthority());
//        }
//        return String.join(",", set);
//    }
    
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return request.getServletPath().equals("/api/auth/logIn");
//    }


    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/auth/login")  ||request.getServletPath().equals("/api/auth/signup");
    }
}