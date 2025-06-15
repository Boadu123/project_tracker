package com.example.project_tracker.controllers;

import com.example.project_tracker.DTO.request.LoginRequestDTO;
import com.example.project_tracker.DTO.request.UserRequestDTO;
import com.example.project_tracker.DTO.response.LoginResponseDTO;
import com.example.project_tracker.service.AuditLogService;
import com.example.project_tracker.service.AuthService;
import com.example.project_tracker.service.UserService;
import com.example.project_tracker.utils.SucessResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final AuditLogService auditLogService;


    public AuthController(AuthService authService, UserService userService, AuditLogService auditLogService) {
        this.authService = authService;
        this.userService = userService;
        this.auditLogService = auditLogService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO requestDTO) {
        try {
            LoginResponseDTO responseDTO = authService.login(requestDTO);
            auditLogService.logAction("LOGIN_SUCCESS","User",null, requestDTO.getEmail(),
                    "User logged in successfully"
            );
            return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, responseDTO);
        } catch (BadCredentialsException ex) {
            auditLogService.logAction("LOGIN_FAILED","User",null,requestDTO.getEmail(),
                    "Invalid login credentials"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        userService.registerUser(requestDTO);
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, "User registered successfully");
    }
}
