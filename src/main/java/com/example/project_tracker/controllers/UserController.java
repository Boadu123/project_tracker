package com.example.project_tracker.controllers;

import com.example.project_tracker.DTO.request.UserRequestDTO;
import com.example.project_tracker.DTO.response.TaskResponseDTO;
import com.example.project_tracker.DTO.response.UserResponseDTO;
import com.example.project_tracker.service.TaskService;
import com.example.project_tracker.service.UserService;
import com.example.project_tracker.utils.SucessResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.project_tracker.utils.SucessResponseUtil.sucessResponseUtil;

/**
 * UserController handles endpoints for user profile and management actions.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    public UserController(UserService userService , TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    /** Returns the currently logged-in user's details. */
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser() {
        UserResponseDTO userResponse = userService.getLoggedInUserDetails();
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, userResponse);
    }

    /** Updates the profile of the logged-in user. */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO updatedUser = userService.updateUserDetails(requestDTO);
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, updatedUser);
    }

    /** Deletes the account of the logged-in user. */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        userService.deleteLoggedInUser();
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, "User account deleted successfully");
    }

    /** Returns a list of all users. */
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, users);
    }

    /** Deletes a user by their ID. */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, "User deleted successfully");
    }

    /**
     * GET endpoint to fetch all tasks assigned to a specific user. */
    @GetMapping("/{userId}/task")
    public ResponseEntity<Map<String, Object>> getTasksByUserId(@PathVariable Long userId) {
        List<TaskResponseDTO> response = taskService.getTasksByUserId(userId);
        return sucessResponseUtil(HttpStatus.OK, response);
    }
}
