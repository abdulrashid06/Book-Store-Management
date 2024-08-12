package com.bookstore.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bookstore.service.ManualUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



@Configuration
@EnableWebSecurity
public class Config implements WebMvcConfigurer {
	
	@Autowired
    private ManualUserDetailService manualUserDetailService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(cors -> cors
                .configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                    return configuration;
                }))
            .authorizeHttpRequests(authz -> authz
                    .requestMatchers("/swagger-ui*/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/books/get_book_list").permitAll()
                    //Users Api's
                    .requestMatchers(HttpMethod.PUT, "/api/users/update/{id}").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/api/orders/create").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/api/orders/get_order_list").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT, "/api/orders/cancell/{orderId}").hasRole("USER")
                    // Admin's Api
                    .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/orders/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/orders/status/{orderId}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/books/addBooks").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/books/get_book_list").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
                    .anyRequest().authenticated())
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(new JwtTokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class);
//            .addFilterAfter(new JwtTokenGeneratorFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(manualUserDetailService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
    
    
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        return objectMapper;
//    }

}
