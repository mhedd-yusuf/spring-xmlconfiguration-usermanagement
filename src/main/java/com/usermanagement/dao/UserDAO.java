package com.usermanagement.dao;

import com.usermanagement.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    List<User> findByStatus(User.UserStatus status);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User update(User user);
    void delete(Long id);
    long count();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
