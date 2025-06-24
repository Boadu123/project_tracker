package com.example.project_tracker.service;

import com.example.project_tracker.DTO.request.LoginRequestDTO;
import com.example.project_tracker.DTO.response.LoginResponseDTO;
import com.example.project_tracker.aspects.LoginAudit;
import com.example.project_tracker.security.JwtUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @LoginAudit
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )
        );

        String token = jwtUtils.generateToken(requestDTO.getEmail());
        return new LoginResponseDTO(token);
    }
}
