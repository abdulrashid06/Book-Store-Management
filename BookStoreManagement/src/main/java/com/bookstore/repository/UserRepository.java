package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Method to find a User by their username
    Optional<User> findByUsername(String username);
    
    // Method to find a User by their email
    Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}