package com.example.project_tracker.controllers;

import com.example.project_tracker.DTO.response.LoginResponseDTO;
import com.example.project_tracker.security.JwtUtils;
import com.example.project_tracker.utils.SucessResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final JwtUtils jwtUtils;

    @GetMapping("/success")
    public ResponseEntity<?> oauth2Success(Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String token = jwtUtils.generateToken(email);

        LoginResponseDTO responseDTO = new LoginResponseDTO(token);

        return SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, responseDTO);
    }
}
