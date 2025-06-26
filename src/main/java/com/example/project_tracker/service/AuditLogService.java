package com.example.project_tracker.service;

import com.example.project_tracker.models.AuditLog;
import com.example.project_tracker.repository.AuditLogRepository;
import com.example.project_tracker.service.interfaces.AuditLogServiceInterface;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for logging and retrieving audit actions performed
 * within the application.
 * <p>
 * Each audit entry records the action type, entity involved, actor, and a data snapshot.
 * Logs can be queried by entity type, actor name, or retrieved in full.
 */
@Service
public class AuditLogService implements AuditLogServiceInterface {

    private final AuditLogRepository auditLogRepository;

    /**
     * Constructs the AuditLogService with the necessary repository.
     *
     * @param auditLogRepository the repository for persisting and retrieving audit logs
     */
    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Logs an action performed on a given entity.
     *
     * @param actionType   the type of action performed (e.g., CREATE, UPDATE, DELETE)
     * @param entityType   the type of entity the action was performed on (e.g., User, Task)
     * @param entityId     the unique ID of the entity
     * @param actorName    the name of the user who performed the action
     * @param dataSnapshot the state or payload of the entity at the time of action
     */
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

    /**
     * Retrieves all audit logs filtered by a specific entity type.
     *
     * @param entityType the type of entity to filter logs by (e.g., Task, Project)
     * @return a list of matching audit logs
     */
    public List<AuditLog> getLogsByEntityType(String entityType) {
        return auditLogRepository.findAll().stream()
                .filter(log -> log.getEntityType().equalsIgnoreCase(entityType))
                .toList();
    }

    /**
     * Retrieves all audit logs filtered by the actor's name.
     *
     * @param actorName the name of the actor to filter logs by
     * @return a list of audit logs created by the given actor
     */
    public List<AuditLog> getLogsByActorName(String actorName) {
        return auditLogRepository.findAll().stream()
                .filter(log -> log.getActorName().equalsIgnoreCase(actorName))
                .toList();
    }

    /**
     * Retrieves all audit logs without any filters.
     *
     * @return a complete list of audit logs
     */
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
