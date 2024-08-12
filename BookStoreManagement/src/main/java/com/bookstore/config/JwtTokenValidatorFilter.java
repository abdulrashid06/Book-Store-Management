package com.bookstore.config;

import java.io.IOException;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

public class JwtTokenValidatorFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String jwt= request.getHeader(SecurityDetails.JWT_HEADER);
		System.out.println(jwt);
		if(jwt != null && jwt.startsWith("Bearer")) {

			try {
				String token  = jwt.substring(7).trim();
				SecretKey key= Keys.hmacShaKeyFor(SecurityDetails.JWT_KEY.getBytes());
				Claims claims= Jwts.parserBuilder().setSigningKey(key).build().
				parseClaimsJws(token).getBody();
				String username= String.valueOf(claims.get("email"));
				System.out.println(username);
				String authorities = (String) claims.get("authorities");
				System.out.println(authorities);
				Authentication auth = new UsernamePasswordAuthenticationToken(username, token, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
				SecurityContextHolder.getContext().setAuthentication(auth);
				System.out.println("validation sucess");
			} catch (Exception e) {
				System.out.println("Invalid token inside validator");
				System.out.println(e.getMessage());
			}

		}
		filterChain.doFilter(request, response);
	}

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/auth/signup");
    }
}

