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

    private LocalDate dueDate;

    @NotNull(message = "Project ID can't be null")
    private Long projectId;

    @NotNull(message = "Developer ID can't be null")
    private Long developerId;

    public TaskRequestDTO() {}

    public TaskRequestDTO(String title, String description, TaskStatus status, LocalDate dueDate, Long projectId, Long developerId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.projectId = projectId;
        this.developerId = developerId;
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

        public TaskRequestDTO build() {
            return new TaskRequestDTO(title, description, status, dueDate, projectId, developerId);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
