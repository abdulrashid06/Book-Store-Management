package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.exception.DuplicateDataException;
import com.bookstore.exception.UserNotFoundException;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	
	
	
	@Override
	public User registerUser(User user) {
		Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new DuplicateDataException("Username already exists.");
        }
        return userRepository.save(user);
	}

	
	@Override
	public User updateUser(Long userId, User updatedUser) {
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(user.getUsername())) {
            Optional<User> existingUser = userRepository.findByUsername(updatedUser.getUsername());
            if (existingUser.isPresent()) {
                throw new DuplicateDataException("Username already exists.");
            }
            user.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }
        return userRepository.save(user);
	}

	
	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
	}

	
	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
	}

	
	@Override
	public List<User> getAllUsers() {
		List<User> userList = userRepository.findAll();
		if(userList.isEmpty()) {
			throw new UserNotFoundException("User List is Empty");
		}
		return userList;
	}

	
	@Override
	public void deleteUser(Long userId) {
		if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
	}
	
	
	

}
