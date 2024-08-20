package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.config.JwtUtils;
import com.bookstore.exception.DuplicateDataException;
import com.bookstore.exception.UserNotFoundException;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository; // Dependency injection of UserRepository

    
    
    /**
     * Registers a new user in the repository. Checks for existing usernames to avoid duplicates.
     * Throws DuplicateDataException if a user with the same username already exists.
     * 
     * @param user The User object to be registered.
     * @return The saved User object.
     */
    @Override
    public User registerUser(User user) {
        // Check if a user with the same username already exists
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            // If found, throw DuplicateDataException
            throw new DuplicateDataException("Username already exists.");
        }
        // Save the new user to the repository
        return userRepository.save(user);
    }

    
    
    
    /**
     * Updates the user information based on the provided token and updated user details.
     * Throws UserNotFoundException if the user is not found with the email extracted from the token.
     * Throws DuplicateDataException if the new username already exists.
     * 
     * @param token The JWT token to decode the user's email.
     * @param updatedUser The User object containing updated details.
     * @return The updated User object.
     */
    @Override
    public User updateUser(String token, User updatedUser) {
        // Decode the token to get the user's email
        String email = JwtUtils.decodeJwt(token);
        // Find the user by email, or throw UserNotFoundException if not found
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + user.get().getId());
        }

        // Check if the username is being updated and if the new username already exists
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(user.get().getUsername())) {
            Optional<User> existingUser = userRepository.findByUsername(updatedUser.getUsername());
            if (existingUser.isPresent()) {
                // If a user with the new username exists, throw DuplicateDataException
                throw new DuplicateDataException("Username already exists.");
            }
            // Update the username
            user.get().setUsername(updatedUser.getUsername());
        }
        // Update other user fields if they are not null
        if (updatedUser.getEmail() != null) {
            user.get().setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            user.get().setPassword(updatedUser.getPassword());
        }
        // Save the updated user to the repository
        return userRepository.save(user.get());
    }

    
    
    
    
    /**
     * Retrieves a user by decoding the email from the provided token.
     * Throws UserNotFoundException if the user is not found with the email extracted from the token.
     * 
     * @param token The JWT token to decode the user's email.
     * @return The User object with the email obtained from the token.
     */
    @Override
    public User getUserById(String token) {
        // Decode the token to get the user's email
        String email = JwtUtils.decodeJwt(token);
        // Find the user by email, or throw UserNotFoundException if not found
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + user.get().getId());
        }
        // Return the user object
        return user.get();
    }

    
    
    
    
    /**
     * Retrieves a user by their username.
     * Throws UserNotFoundException if no user is found with the specified username.
     * 
     * @param username The username of the user to be retrieved.
     * @return The User object with the specified username.
     */
    @Override
    public User getUserByUsername(String username) {
        // Find the user by username, or throw UserNotFoundException if not found
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    
    
    
    /**
     * Retrieves all users from the repository.
     * Throws UserNotFoundException if the user list is empty.
     * 
     * @return A list of all User objects.
     */
    @Override
    public List<User> getAllUsers() {
        // Retrieve all users from the repository
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            // If no users are found, throw UserNotFoundException
            throw new UserNotFoundException("User List is Empty");
        }
        // Return the list of users
        return userList;
    }

    
    
    
    /**
     * Deletes a user identified by the token. The token is used to decode the user's email.
     * Throws UserNotFoundException if the user is not found with the email extracted from the token.
     * 
     * @param token The JWT token to decode the user's email.
     */
    @Override
    public void deleteUser(String token) {
        // Decode the token to get the user's email
        String email = JwtUtils.decodeJwt(token);
        // Find the user by email, or throw UserNotFoundException if not found
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + user.get().getId());
        }
        // Delete the user from the repository by ID
        userRepository.deleteById(user.get().getId());
    }
}
