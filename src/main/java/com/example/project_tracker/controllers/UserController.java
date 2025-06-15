package com.example.project_tracker.controllers;

import com.example.project_tracker.DTO.request.UserRequestDTO;
import com.example.project_tracker.DTO.response.UserResponseDTO;
import com.example.project_tracker.service.UserService;
import com.example.project_tracker.utils.SucessResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser() {
        UserResponseDTO userResponse = userService.getLoggedInUserDetails();
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, userResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO updatedUser = userService.updateUserDetails(requestDTO);
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        userService.deleteLoggedInUser();
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, "User account deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, "User deleted successfully");
    }
}
