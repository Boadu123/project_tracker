package com.example.project_tracker.security;

import com.example.project_tracker.DTO.response.LoginResponseDTO;
import com.example.project_tracker.utils.SucessResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            // Extract user info
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            String email = oidcUser.getEmail();

            // Generate JWT token
            String token = jwtUtils.generateToken(email);

            // Build response object
            LoginResponseDTO responseDTO = new LoginResponseDTO(token);

            // Send JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(
                    response.getWriter(),
                    SucessResponseUtil.sucessResponseUtil(HttpStatus.OK, responseDTO).getBody()
            );

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 login failed");
        }
    }
}
