package com.example.project_tracker.security.handlers;

import com.example.project_tracker.annotations.Auditable;
import com.example.project_tracker.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom implementation of {@link AccessDeniedHandler} that sends a standardized 403 Forbidden
 * response when access is denied for an authenticated user.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final AuditLogService auditLogService;

    public CustomAccessDeniedHandler(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Auditable(actionType = "ACCESS_DENIED", entityType = "Security")
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(String.format(
                """
                {
                    "message": "Authorization failed: %s",
                    "timestamp": "%s",
                    "status": 403
                }
                """,
                accessDeniedException.getMessage(),
                java.time.Instant.now().toString()
        ));
    }
}