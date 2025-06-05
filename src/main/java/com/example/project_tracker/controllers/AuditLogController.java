package com.example.project_tracker.controllers;

import com.example.project_tracker.models.AuditLog;
import com.example.project_tracker.service.AuditLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AuditLog> getAllLogs() {
        return auditLogService.getAllLogs();
    }

    @GetMapping("/entity/{entityType}")
    public List<AuditLog> getLogsByEntityType(@PathVariable String entityType) {
        return auditLogService.getLogsByEntityType(entityType);
    }

    @GetMapping("/actor/{actorName}")
    public List<AuditLog> getLogsByActorName(@PathVariable String actorName) {
        return auditLogService.getLogsByActorName(actorName);
    }
}
