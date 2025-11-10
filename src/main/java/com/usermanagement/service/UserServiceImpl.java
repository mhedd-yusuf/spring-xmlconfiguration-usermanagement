package com.usermanagement.service;

import com.usermanagement.dao.UserDAO;
import com.usermanagement.dto.UserDTO;
import com.usermanagement.exception.UserNotFoundException;
import com.usermanagement.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDAO userDAO;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating new user with username: {}", userDTO.getUsername());

        if (userDAO.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDTO.getUsername());
        }

        if (userDAO.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDTO.getEmail());
        }

        User user = userDTO.toEntity();
        User savedUser = userDAO.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getId());

        return new UserDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        logger.debug("Fetching user with ID: {}", id);
        User user = userDAO.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return new UserDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        logger.debug("Fetching all users");
        List<User> users = userDAO.findAll();
        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByStatus(User.UserStatus status) {
        logger.debug("Fetching users with status: {}", status);
        List<User> users = userDAO.findByStatus(status);
        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        logger.info("Updating user with ID: {}", id);

        User existingUser = userDAO.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (!existingUser.getUsername().equals(userDTO.getUsername()) &&
                userDAO.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDTO.getUsername());
        }

        if (!existingUser.getEmail().equals(userDTO.getEmail()) &&
                userDAO.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDTO.getEmail());
        }

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(userDTO.getPassword());
        }

        if (userDTO.getStatus() != null) {
            existingUser.setStatus(userDTO.getStatus());
        }

        if (userDTO.getRole() != null) {
            existingUser.setRole(userDTO.getRole());
        }

        User updatedUser = userDAO.update(existingUser);
        logger.info("User updated successfully with ID: {}", updatedUser.getId());

        return new UserDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);

        if (!userDAO.findById(id).isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        userDAO.delete(id);
        logger.info("User deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUserCount() {
        return userDAO.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userDAO.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userDAO.existsByEmail(email);
    }
}
