package com.example.project_tracker.service.interfaces;

import com.example.project_tracker.DTO.request.ProjectRequestDTO;
import com.example.project_tracker.DTO.response.ProjectResponseDTO;

import jakarta.validation.Valid;
import java.util.List;

public interface ProjectServiceInterface {

    ProjectResponseDTO createProject(@Valid ProjectRequestDTO requestDTO);

    List<ProjectResponseDTO> getAllProjects();

    ProjectResponseDTO getProjectById(Long id);

    ProjectResponseDTO updateProject(Long id, @Valid ProjectRequestDTO requestDTO);

    void deleteProject(Long id);
}
