package com.example.project_tracker.DTO.request;

import com.example.project_tracker.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public class ProjectRequestDTO {

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    @NotNull(message = "Deadline date is required")
    private LocalDate deadline;

    @NotNull(message = "Status is required")
    private ProjectStatus status;

    @NotNull(message = "task field is required")
    private Set<String> task;

    public ProjectRequestDTO() {}

    public ProjectRequestDTO(String name, String description, LocalDate deadline, ProjectStatus status) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public static class Builder {
        private String name;
        private String description;
        private LocalDate deadline;
        private ProjectStatus status;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder deadline(LocalDate deadline) {
            this.deadline = deadline;
            return this;
        }

        public Builder status(ProjectStatus status) {
            this.status = status;
            return this;
        }

        public ProjectRequestDTO build() {
            return new ProjectRequestDTO(name, description, deadline, status);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
