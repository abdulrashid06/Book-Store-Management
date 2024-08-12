package com.bookstore.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.config.JwtUtils;
import com.bookstore.config.SecurityDetails;
import com.bookstore.model.User;
import com.bookstore.model.User.Role;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.UserServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> logInUserHandler(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Optional<User> opt = userRepository.findByEmail(loginRequest.getEmail());
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = opt.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        // Generate token
        String token = jwtUtils.generateToken(user);

        // Prepare response with user details and token
        response.setHeader(SecurityDetails.JWT_HEADER, "Bearer " + token);

        return ResponseEntity.ok().body("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("User logged out successfully", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole()==null) {
        	
        	user.setRole("USER");
        }
        userService.registerUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
    
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
    
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User userProfile = userRepository.findByEmail(username).get();
        return ResponseEntity.ok(userProfile);
    }
    
}

@Data
class LoginRequest {
	
	private String email;
	private String password;

}