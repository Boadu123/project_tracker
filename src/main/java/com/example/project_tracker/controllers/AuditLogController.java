package com.example.project_tracker.controllers;

import com.example.project_tracker.models.AuditLog;
import com.example.project_tracker.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.project_tracker.utils.SucessResponseUtil.sucessResponseUtil;

/**
 * Exposes endpoints for retrieving audit logs by type or actor.
 */
@RestController
@RequestMapping("/api/logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    /** Retrieves all audit logs. */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLogs() {
        List<AuditLog> logs = auditLogService.getAllLogs();
        return sucessResponseUtil(HttpStatus.OK, logs);
    }

    /** Retrieves logs filtered by entity type. */
    @GetMapping("/entity/{entityType}")
    public ResponseEntity<Map<String, Object>> getLogsByEntityType(@PathVariable String entityType) {
        List<AuditLog> logs = auditLogService.getLogsByEntityType(entityType);
        return sucessResponseUtil(HttpStatus.OK, logs);
    }

    /** Retrieves logs filtered by actor name. */
    @GetMapping("/actor/{actorName}")
    public ResponseEntity<Map<String, Object>> getLogsByActorName(@PathVariable String actorName) {
        List<AuditLog> logs = auditLogService.getLogsByActorName(actorName);
        return sucessResponseUtil(HttpStatus.OK, logs);
    }
}
