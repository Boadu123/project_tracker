package com.example.project_tracker.service.interfaces;

import com.example.project_tracker.models.AuditLog;

import java.util.List;

public interface AuditLogServiceInterface {

    void logAction(String actionType, String entityType, String entityId, String actorName, Object dataSnapshot);

    List<AuditLog> getLogsByEntityType(String entityType);

    List<AuditLog> getLogsByActorName(String actorName);

    List<AuditLog> getAllLogs();
}
