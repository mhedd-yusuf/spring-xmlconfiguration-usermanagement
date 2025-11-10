package com.usermanagement.controller;

import com.usermanagement.dto.UserDTO;
import com.usermanagement.model.User;
import com.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserViewController {

    private static final Logger logger = LoggerFactory.getLogger(UserViewController.class);

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(
            @RequestParam(required = false) String status,
            Model model) {
        logger.info("VIEW: Displaying users list");

        List<UserDTO> users;
        if (status != null && !status.isEmpty()) {
            users = userService.getUsersByStatus(User.UserStatus.valueOf(status.toUpperCase()));
            model.addAttribute("filterStatus", status);
        } else {
            users = userService.getAllUsers();
        }

        model.addAttribute("users", users);
        model.addAttribute("totalCount", userService.getUserCount());
        model.addAttribute("statuses", User.UserStatus.values());

        return "users";
    }

    @GetMapping("/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        logger.info("VIEW: Displaying user detail for ID: {}", id);
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("VIEW: Displaying create user form");
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("statuses", User.UserStatus.values());
        model.addAttribute("roles", User.UserRole.values());
        model.addAttribute("formAction", "create");
        return "user-form";
    }

    @PostMapping
    public String createUser(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        logger.info("VIEW: Processing create user form");

        if (result.hasErrors()) {
            model.addAttribute("statuses", User.UserStatus.values());
            model.addAttribute("roles", User.UserRole.values());
            model.addAttribute("formAction", "create");
            return "user-form";
        }

        try {
            UserDTO createdUser = userService.createUser(userDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "User created successfully: " + createdUser.getUsername());
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statuses", User.UserStatus.values());
            model.addAttribute("roles", User.UserRole.values());
            model.addAttribute("formAction", "create");
            return "user-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("VIEW: Displaying edit user form for ID: {}", id);
        UserDTO user = userService.getUserById(id);
        model.addAttribute("userDTO", user);
        model.addAttribute("statuses", User.UserStatus.values());
        model.addAttribute("roles", User.UserRole.values());
        model.addAttribute("formAction", "edit");
        return "user-form";
    }

    @PostMapping("/{id}")
    public String updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        logger.info("VIEW: Processing update user form for ID: {}", id);

        if (result.hasErrors()) {
            model.addAttribute("statuses", User.UserStatus.values());
            model.addAttribute("roles", User.UserRole.values());
            model.addAttribute("formAction", "edit");
            return "user-form";
        }

        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "User updated successfully: " + updatedUser.getUsername());
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statuses", User.UserStatus.values());
            model.addAttribute("roles", User.UserRole.values());
            model.addAttribute("formAction", "edit");
            return "user-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        logger.info("VIEW: Deleting user with ID: {}", id);
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "User deleted successfully");
        return "redirect:/users";
    }
}
