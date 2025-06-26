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

/**
 * Custom handler invoked when an OAuth2 authentication is successful.
 * <p>
 * This handler generates a JWT token based on the authenticated OIDC user’s email,
 * wraps it in a {@link LoginResponseDTO}, and returns it as a JSON response to the client.
 */
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    /**
     * Called when a user has been successfully authenticated via OAuth2.
     * <p>
     * This implementation extracts the user's email from the {@link OidcUser},
     * generates a JWT token, wraps it in a success response DTO, and writes it to the response.
     *
     * @param request        the incoming HTTP request
     * @param response       the HTTP response to send back
     * @param authentication the authentication object containing the OIDC user
     * @throws IOException      in case of I/O errors
     * @throws ServletException in case of servlet errors
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            // Extract authenticated user's email from OIDC
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            String email = oidcUser.getEmail();

            // Generate JWT token based on email
            String token = jwtUtils.generateToken(email);

            // Wrap token in response DTO
            LoginResponseDTO responseDTO = new LoginResponseDTO(token);

            // Configure response headers and return JSON
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
