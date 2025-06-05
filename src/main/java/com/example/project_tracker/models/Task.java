package com.example.project_tracker.models;

import com.example.project_tracker.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDate dueDate;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "developer_id")
    private Long developerId;

    public Task() {}

    public Task(Long id, String title, String description, TaskStatus status, LocalDate dueDate, Long projectId, Long developerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.projectId = projectId;
        this.developerId = developerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(Long developerId) {
        this.developerId = developerId;
    }

    public static class Builder {
        private String title;
        private String description;
        private TaskStatus status;
        private LocalDate dueDate;
        private Long projectId;
        private Long developerId;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder projectId(Long projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder developerId(Long developerId) {
            this.developerId = developerId;
            return this;
        }

        public Task build() {
            Task task = new Task();
            task.setTitle(this.title);
            task.setDescription(this.description);
            task.setStatus(this.status);
            task.setDueDate(this.dueDate);
            task.setProjectId(this.projectId);
            task.setDeveloperId(this.developerId);
            return task;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
