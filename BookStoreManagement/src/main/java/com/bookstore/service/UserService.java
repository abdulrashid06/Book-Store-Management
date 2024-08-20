package com.bookstore.service;

import java.util.List;

import com.bookstore.model.User;

public interface UserService {
	
User registerUser(User user);
    
    public User updateUser(String token, User updatedUser);
    
    public User getUserById(String token);
    
    public User getUserByUsername(String username);
    
    public List<User> getAllUsers();
    
    public void deleteUser(String token);

}
