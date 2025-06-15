package com.example.project_tracker.security.handlers;

import com.example.project_tracker.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final AuditLogService auditLogService;

    public CustomAccessDeniedHandler(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        String username = request.getUserPrincipal() != null
                ? request.getUserPrincipal().getName()
                : "Anonymous";
        String attemptedPath = request.getRequestURI();

        auditLogService.logAction(
                "ACCESS_DENIED",
                "Security",
                null,
                username,
                "Access denied to: " + attemptedPath
        );

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }
}
