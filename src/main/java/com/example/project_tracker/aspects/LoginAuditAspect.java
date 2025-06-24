package com.example.project_tracker.aspects;

import com.example.project_tracker.DTO.request.LoginRequestDTO;
import com.example.project_tracker.service.AuditLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginAuditAspect {

    @Autowired
    private AuditLogService auditLogService;

    @Around("@annotation(com.example.project_tracker.aspects.LoginAudit)")
    public Object logLoginAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        LoginRequestDTO loginRequest = null;

        // Find LoginRequestDTO in method arguments
        for (Object arg : args) {
            if (arg instanceof LoginRequestDTO) {
                loginRequest = (LoginRequestDTO) arg;
                break;
            }
        }

        try {
            Object result = joinPoint.proceed(); // Proceed to login method
            auditLogService.logAction(
                    "LOGIN_SUCCESS",
                    "User",
                    null,
                    loginRequest != null ? loginRequest.getEmail() : null,
                    "User logged in successfully"
            );
            return result;
        } catch (BadCredentialsException ex) {
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

