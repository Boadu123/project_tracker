package com.example.project_tracker.controllers;

import com.example.project_tracker.DTO.request.LoginRequestDTO;
import com.example.project_tracker.DTO.request.UserRequestDTO;
import com.example.project_tracker.DTO.response.LoginResponseDTO;
import com.example.project_tracker.aspects.LoginAudit;
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
        LoginResponseDTO responseDTO = authService.login(requestDTO);
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        userService.registerUser(requestDTO);
        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, "User registered successfully");
    }
}
