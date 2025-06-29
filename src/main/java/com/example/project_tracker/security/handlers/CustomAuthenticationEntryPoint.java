package com.example.project_tracker.security.handlers;

import com.example.project_tracker.annotations.Auditable;
import com.example.project_tracker.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom implementation of {@link AuthenticationEntryPoint} that handles unauthorized access attempts.
 * <p>
 * This is triggered when a user tries to access a protected resource without proper authentication.
 * It logs the incident (via the {@link Auditable} annotation) and sends a standardized 401 Unauthorized response.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuditLogService auditLogService;

    public CustomAuthenticationEntryPoint(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Auditable(actionType = "ACCESS_DENIED", entityType = "Security")
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(String.format(
                """
                {
                    "message": "Authentication failed: %s",
                    "timestamp": "%s",
                    "status": 401
                }
                """,
                authException.getMessage(),
                java.time.Instant.now().toString()
        ));
    }
}