package com.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.model.User;
import com.bookstore.service.UserServiceImpl;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    
    
    

    /**
     * Handles the registration of a new user. This endpoint accepts the user details 
     * in the request body and saves the user to the database. If the user is successfully 
     * created, it returns a response with the created user and an HTTP status of 201 (Created).
     *
     * @param user The user object to be created.
     * @return ResponseEntity containing the created User and HTTP status 201.
     */
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.registerUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    
    
    
    

    /**
     * Retrieves a specific user by their ID. This endpoint returns the user 
     * if found, along with an HTTP status of 200 (OK).
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the requested User and HTTP status 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    
    
    
    
    /**
     * Retrieves a list of all users in the system. This endpoint returns a 
     * list of users, along with an HTTP status of 200 (OK).
     *
     * @return ResponseEntity containing a list of Users and HTTP status 200.
     */
    @GetMapping("/get_all_user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    
    
    
    
    /**
     * Updates an existing user by their ID. This endpoint accepts the user details 
     * in the request body and updates the corresponding user in the database. 
     * The response contains the updated user and an HTTP status of 202 (Accepted).
     *
     * @param id The ID of the user to update.
     * @param userDetails The details of the user to be updated.
     * @return ResponseEntity containing the updated User and HTTP status 202.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
    }

    
    
    
    
    /**
     * Deletes a specific user by their ID. This endpoint deletes the user if they exist 
     * and returns an HTTP status of 204 (No Content) to indicate that the deletion was successful.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity with HTTP status 204 indicating that the user was successfully deleted.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
}
