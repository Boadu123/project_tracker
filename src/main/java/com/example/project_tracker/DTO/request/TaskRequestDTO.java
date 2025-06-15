package com.example.project_tracker.DTO.request;

import com.example.project_tracker.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TaskRequestDTO {

    @NotBlank(message = "Title can't be blank")
    private String title;

    private String description;

    @NotNull(message = "Status can't be null")
    private TaskStatus status;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "User ID is required")
    private Long userId;

    public TaskRequestDTO() {}

    public TaskRequestDTO(String title, String description, TaskStatus status, LocalDate dueDate, Long projectId, Long userId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.projectId = projectId;
        this.userId = userId;
    }

    // Getters and Setters

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static class Builder {
        private String title;
        private String description;
        private TaskStatus status;
        private LocalDate dueDate;
        private Long projectId;
        private Long userId;

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

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public TaskRequestDTO build() {
            return new TaskRequestDTO(title, description, status, dueDate, projectId, userId);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
