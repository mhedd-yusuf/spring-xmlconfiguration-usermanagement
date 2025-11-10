package com.usermanagement.controller;

import com.usermanagement.dto.UserDTO;
import com.usermanagement.model.User;
import com.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(required = false) String status) {
        logger.info("REST: Fetching all users");

        List<UserDTO> users;
        if (status != null && !status.isEmpty()) {
            users = userService.getUsersByStatus(User.UserStatus.valueOf(status.toUpperCase()));
        } else {
            users = userService.getAllUsers();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("count", users.size());
        response.put("totalCount", userService.getUserCount());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.info("REST: Fetching user with ID: {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserDTO userDTO) {
        logger.info("REST: Creating new user: {}", userDTO.getUsername());
        UserDTO createdUser = userService.createUser(userDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User created successfully");
        response.put("user", createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        logger.info("REST: Updating user with ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, userDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User updated successfully");
        response.put("user", updatedUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        logger.info("REST: Deleting user with ID: {}", id);
        userService.deleteUser(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("id", id.toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(
            @RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(
            @RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUserCount() {
        long count = userService.getUserCount();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
