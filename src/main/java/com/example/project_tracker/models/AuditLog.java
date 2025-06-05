package com.example.project_tracker.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;
    private String actionType;
    private String entityType;
    private String entityId;
    private Instant timestamp;
    private String actorName;

    @Field("payload")
    private Map<String, Object> payload;

    public AuditLog() {}

    public AuditLog(String id, String actionType, String entityType, String entityId, Instant timestamp, String actorName, Map<String, Object> payload) {
        this.id = id;
        this.actionType = actionType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.timestamp = timestamp;
        this.actorName = actorName;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}
