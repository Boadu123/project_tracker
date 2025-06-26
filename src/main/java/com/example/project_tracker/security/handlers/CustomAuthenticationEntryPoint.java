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
 * It logs the incident (via the {@link Auditable} annotation) and sends a 401 Unauthorized HTTP response.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuditLogService auditLogService;

    /**
     * Constructs the custom authentication entry point with dependency injection for auditing.
     *
     * @param auditLogService the service responsible for persisting audit logs
     */
    public CustomAuthenticationEntryPoint(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    /**
     * Handles an unauthorized request by returning a 401 error and triggering auditing.
     *
     * @param request       the incoming HTTP request
     * @param response      the HTTP response to be sent
     * @param authException the exception that caused the authentication failure
     * @throws IOException if an input or output error occurs
     */
    @Auditable(actionType = "ACCESS_DENIED", entityType = "Security")
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
