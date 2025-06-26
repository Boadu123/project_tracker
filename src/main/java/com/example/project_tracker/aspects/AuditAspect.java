package com.example.project_tracker.aspects;

import com.example.project_tracker.annotations.Auditable;
import com.example.project_tracker.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Aspect for logging actions annotated with @Auditable after successful execution.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;

    /**
     * Logs audit information after a method annotated with @Auditable completes.
     *
     * @param joinPoint the intercepted method
     * @param result    the returned object from the method
     */
    @AfterReturning(pointcut = "@annotation(com.example.project_tracker.annotations.Auditable)", returning = "result")
    public void logAudit(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Auditable auditable = methodSignature.getMethod().getAnnotation(Auditable.class);

        String actionType = auditable.actionType();
        String entityType = auditable.entityType();
        String entityId = extractEntityIdFromArgs(joinPoint.getArgs());
        String actorName = getCurrentUser();

        auditLogService.logAction(
                actionType,
                entityType,
                entityId,
                actorName,
                result
        );
    }

    /**
     * Extracts entity ID from method arguments if present, else generates a UUID.
     */
    private String extractEntityIdFromArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Long) {
                return arg.toString();
            }
        }
        return UUID.randomUUID().toString();
    }

    /**
     * Retrieves the current authenticated user's name or returns 'SYSTEM'.
     */
    private String getCurrentUser() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "SYSTEM";
        }
    }
}
