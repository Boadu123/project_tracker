package com.example.project_tracker.service;

import com.example.project_tracker.DTO.request.ProjectRequestDTO;
import com.example.project_tracker.DTO.response.ProjectResponseDTO;
import com.example.project_tracker.annotations.Auditable;
import com.example.project_tracker.exceptions.ProjectNotFoundException;
import com.example.project_tracker.mapper.ProjectMapper;
import com.example.project_tracker.models.Project;
import com.example.project_tracker.models.User;
import com.example.project_tracker.repository.ProjectRepository;
import com.example.project_tracker.repository.TaskRepository;
import com.example.project_tracker.security.CustomUserDetails;
import com.example.project_tracker.service.interfaces.ProjectServiceInterface;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService implements ProjectServiceInterface {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AuditLogService auditLogService;

    public ProjectService(ProjectRepository projectRepository, AuditLogService auditLogService, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.auditLogService = auditLogService;
    }

    @Auditable(actionType = "CREATE", entityType = "Project")
    public ProjectResponseDTO createProject(@Valid ProjectRequestDTO requestDTO) {
        Project project = ProjectMapper.toEntity(requestDTO);
        project.setUser(getAuthenticatedUser());
        return ProjectMapper.toDTO(projectRepository.save(project));
    }

    @Auditable(actionType = "GET", entityType = "Project")
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Auditable(actionType = "GET", entityType = "Project")
    @Cacheable(value = "projectById", key = "#id")
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));
        return ProjectMapper.toDTO(project);
    }


    @Auditable(actionType = "UPDATE", entityType = "Project")
    public ProjectResponseDTO updateProject(Long id, @Valid ProjectRequestDTO requestDTO) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));

        existing.setName(requestDTO.getName());
        existing.setDescription(requestDTO.getDescription());
        existing.setDeadline(requestDTO.getDeadline());
        existing.setStatus(requestDTO.getStatus());

        return ProjectMapper.toDTO(projectRepository.save(existing));
    }

    @Auditable(actionType = "DELETE", entityType = "Project")
    @Transactional
    public void deleteProject(Long id) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));

        taskRepository.deleteByProjectId(id);

        projectRepository.delete(existing);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }
        throw new RuntimeException("User not authenticated");
    }
}
