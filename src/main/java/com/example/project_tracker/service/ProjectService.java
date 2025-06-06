package com.example.project_tracker.service;

import com.example.project_tracker.DTO.request.ProjectRequestDTO;
import com.example.project_tracker.DTO.response.ProjectResponseDTO;
import com.example.project_tracker.exceptions.ProjectNotFoundException;
import com.example.project_tracker.mapper.ProjectMapper;
import com.example.project_tracker.models.Project;
import com.example.project_tracker.repository.ProjectRepository;
import com.example.project_tracker.repository.TaskRepository;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AuditLogService auditLogService;

    public ProjectService(ProjectRepository projectRepository, AuditLogService auditLogService, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.auditLogService = auditLogService;
    }

    public ProjectResponseDTO createProject(@Valid ProjectRequestDTO requestDTO) {
        Project project = ProjectMapper.toEntity(requestDTO);
        Project saved = projectRepository.save(project);
        auditLogService.logAction("CREATE", "Project", saved.getId().toString(), saved.getName().toString(), saved);
        return ProjectMapper.toDTO(saved);
    }

    @Cacheable(value = "projects")
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "projectById", key = "#id")
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));
        return ProjectMapper.toDTO(project);
    }

    public ProjectResponseDTO updateProject(Long id, @Valid ProjectRequestDTO requestDTO) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));

        existing.setName(requestDTO.getName());
        existing.setDescription(requestDTO.getDescription());
        existing.setDeadline(requestDTO.getDeadline());
        existing.setStatus(requestDTO.getStatus());

        Project updated = projectRepository.save(existing);
        auditLogService.logAction("UPDATE", "Project", updated.getId().toString(), updated.getName().toString(), updated);

        return ProjectMapper.toDTO(updated);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));

        taskRepository.deleteByProjectId(id);

        auditLogService.logAction("DELETE", "Project", existing.getId().toString(), existing.getName().toString(), existing);

        projectRepository.delete(existing);
    }
}
