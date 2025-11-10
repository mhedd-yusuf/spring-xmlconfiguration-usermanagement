package com.usermanagement.service;

import com.usermanagement.dto.UserDTO;
import com.usermanagement.model.User;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    List<UserDTO> getUsersByStatus(User.UserStatus status);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    long getUserCount();
    boolean isUsernameAvailable(String username);
    boolean isEmailAvailable(String email);
}
