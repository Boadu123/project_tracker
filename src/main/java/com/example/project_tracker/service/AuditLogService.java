package com.example.project_tracker.service;

import com.example.project_tracker.models.AuditLog;
import com.example.project_tracker.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String actionType, String entityType, String entityId, String actorName, Object dataSnapshot) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("snapshot", dataSnapshot);

        AuditLog log = new AuditLog();
        log.setActionType(actionType);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setActorName(actorName);
        log.setTimestamp(Instant.now());
        log.setPayload(payload);

        auditLogRepository.save(log);
    }

    public List<AuditLog> getLogsByEntityType(String entityType) {
        return auditLogRepository.findAll().stream()
                .filter(log -> log.getEntityType().equalsIgnoreCase(entityType))
                .toList();
    }

    public List<AuditLog> getLogsByActorName(String actorName) {
        return auditLogRepository.findAll().stream()
                .filter(log -> log.getActorName().equalsIgnoreCase(actorName))
                .toList();
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
