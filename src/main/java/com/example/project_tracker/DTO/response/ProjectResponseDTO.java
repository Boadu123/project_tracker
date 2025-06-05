package com.example.project_tracker.DTO.response;

import com.example.project_tracker.enums.ProjectStatus;
import java.time.LocalDate;

public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate deadline;
    private ProjectStatus status;

    public ProjectResponseDTO() {

    }

    public ProjectResponseDTO(Long id, String name, String description, LocalDate deadline, ProjectStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
