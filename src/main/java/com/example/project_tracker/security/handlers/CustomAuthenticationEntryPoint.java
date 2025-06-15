package com.example.project_tracker.security.handlers;

import com.example.project_tracker.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuditLogService auditLogService;

    public CustomAuthenticationEntryPoint(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String attemptedPath = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();

        auditLogService.logAction(
                "UNAUTHORIZED_ACCESS",
                "Security",
                null,
                "Anonymous",
                "Unauthorized access attempt to: " + attemptedPath + " from IP: " + remoteAddr
        );

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}

