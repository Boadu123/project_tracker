package com.example.project_tracker.service;

import com.example.project_tracker.DTO.request.LoginRequestDTO;
import com.example.project_tracker.DTO.response.LoginResponseDTO;
import com.example.project_tracker.aspects.LoginAudit;
import com.example.project_tracker.security.JwtUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling user authentication logic.
 * <p>
 * Uses Spring Security's {@link AuthenticationManager} to authenticate users
 * and generates JWT tokens for successful logins.
 * <p>
 * The {@link LoginAudit} annotation is used to log login attempts.
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * Constructs the AuthService with required dependencies.
     *
     * @param authenticationManager Spring Security authentication manager
     * @param jwtUtils              utility for generating JWT tokens
     */
    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Authenticates the user using the provided credentials and returns a JWT token.
     *
     * @param requestDTO the login request containing email and password
     * @return a response containing the generated JWT token
     * @throws BadCredentialsException if authentication fails due to invalid credentials
     */
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
