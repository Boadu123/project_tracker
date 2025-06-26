package com.example.project_tracker.aspects;

import com.example.project_tracker.DTO.request.LoginRequestDTO;
import com.example.project_tracker.service.AuditLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

/**
 * Aspect for auditing login attempts using the @LoginAudit annotation.
 * Logs both successful and failed login events to the audit trail.
 */
@Aspect
@Component
public class LoginAuditAspect {

    @Autowired
    private AuditLogService auditLogService;

    /**
     * Intercepts methods annotated with @LoginAudit and logs login outcomes.
     *
     * @param joinPoint the join point of the method
     * @return the result of the intercepted method
     * @throws Throwable if the original method throws
     */
    @Around("@annotation(com.example.project_tracker.aspects.LoginAudit)")
    public Object logLoginAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        LoginRequestDTO loginRequest = null;

        // Extract LoginRequestDTO argument for logging
        for (Object arg : args) {
            if (arg instanceof LoginRequestDTO) {
                loginRequest = (LoginRequestDTO) arg;
                break;
            }
        }

        try {
            // Proceed with method execution and log success
            Object result = joinPoint.proceed();
            auditLogService.logAction(
                    "LOGIN_SUCCESS",
                    "User",
                    null,
                    loginRequest != null ? loginRequest.getEmail() : null,
                    "User logged in successfully"
            );
            return result;
        } catch (BadCredentialsException ex) {
            // Log failed login attempt
            auditLogService.logAction(
                    "LOGIN_FAILED",
                    "User",
                    null,
                    loginRequest != null ? loginRequest.getEmail() : null,
                    "Invalid login credentials"
            );
            throw ex;
        }
    }
}
