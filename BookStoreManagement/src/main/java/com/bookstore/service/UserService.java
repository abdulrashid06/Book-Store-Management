package com.bookstore.service;

import java.util.List;

import com.bookstore.model.User;

public interface UserService {
	
User registerUser(User user);
    
    public User updateUser(Long userId, User updatedUser);
    
    public User getUserById(Long userId);
    
    public User getUserByUsername(String username);
    
    public List<User> getAllUsers();
    
    public void deleteUser(Long userId);

}
