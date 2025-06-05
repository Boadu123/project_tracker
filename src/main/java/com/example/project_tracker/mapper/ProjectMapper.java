package com.example.project_tracker.mapper;

import com.example.project_tracker.DTO.request.ProjectRequestDTO;
import com.example.project_tracker.DTO.response.ProjectResponseDTO;
import com.example.project_tracker.models.Project;

public class ProjectMapper {

    public static Project toEntity(ProjectRequestDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setDeadline(dto.getDeadline());
        project.setStatus(dto.getStatus());
        return project;
    }

    public static ProjectResponseDTO toDTO(Project project) {
        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setDeadline(project.getDeadline());
        response.setStatus(project.getStatus());
        return response;
    }
}
