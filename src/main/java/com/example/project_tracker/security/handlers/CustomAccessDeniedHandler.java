package com.example.project_tracker.security.handlers;

import com.example.project_tracker.annotations.Auditable;
import com.example.project_tracker.service.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom implementation of {@link AccessDeniedHandler} that returns a JSON response
 * when a user is authenticated but lacks necessary permissions.
 * <p>
 * This handler also triggers auditing via the {@link Auditable} annotation and returns
 * a structured 403 Forbidden error in JSON format.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs the access denied handler with a dependency on {@link AuditLogService}.
     *
     * @param auditLogService the audit logging service used for tracking security events
     */
    public CustomAccessDeniedHandler(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    /**
     * Handles access denial by returning a structured JSON response with HTTP 403 status.
     * Also triggers auditing of the access denial.
     *
     * @param request                the current HTTP request
     * @param response               the HTTP response to be written
     * @param accessDeniedException the exception indicating access was denied
     * @throws IOException if writing the response fails
     */
    @Auditable(actionType = "ACCESS_DENIED", entityType = "Security")
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        if (response.isCommitted()) {
            return;
        }

        // Build structured JSON error response
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.FORBIDDEN.value());
        error.put("message", "Access denied: " + accessDeniedException.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), error);
    }
}
