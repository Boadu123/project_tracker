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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing project-related operations,
 * including creation, retrieval, update, and deletion of projects.
 * <p>
 * Projects are associated with users and can have related tasks,
 * which are deleted when a project is deleted.
 * <p>
 * Audit logging is applied using the {@link Auditable} annotation.
 */
@Service
public class ProjectService implements ProjectServiceInterface {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AuditLogService auditLogService;

    /**
     * Constructs the ProjectService with required dependencies.
     *
     * @param projectRepository repository for accessing project data
     * @param auditLogService   service for handling audit logging
     * @param taskRepository    repository for managing tasks related to projects
     */
    public ProjectService(ProjectRepository projectRepository, AuditLogService auditLogService, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.auditLogService = auditLogService;
    }

    /**
     * Creates a new project and associates it with the authenticated user.
     *
     * @param requestDTO the project data to create
     * @return the created project as a response DTO
     */
    @Auditable(actionType = "CREATE", entityType = "Project")
    public ProjectResponseDTO createProject(@Valid ProjectRequestDTO requestDTO) {
        Project project = ProjectMapper.toEntity(requestDTO);
        project.setUser(getAuthenticatedUser());
        return ProjectMapper.toDTO(projectRepository.save(project));
    }

    /**
     * Retrieves all projects in the system.
     *
     * @return a list of project response DTOs
     */
    @Auditable(actionType = "GET", entityType = "Project")
    @Cacheable("projects")
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a project by its unique ID. Uses caching to improve performance.
     *
     * @param id the ID of the project
     * @return the project as a response DTO
     * @throws ProjectNotFoundException if no project is found with the given ID
     */
    @Auditable(actionType = "GET", entityType = "Project")
    @Cacheable(value = "projectById", key = "#id")
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));
        return ProjectMapper.toDTO(project);
    }

    /**
     * Updates the project with the given ID using the provided request data.
     *
     * @param id         the ID of the project to update
     * @param requestDTO the new project data
     * @return the updated project as a response DTO
     * @throws ProjectNotFoundException if the project does not exist
     */
    @Auditable(actionType = "UPDATE", entityType = "Project")
    @CacheEvict(value = "tasks", key = "#userId")
    public ProjectResponseDTO updateProject(Long id, @Valid ProjectRequestDTO requestDTO) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));

        existing.setName(requestDTO.getName());
        existing.setDescription(requestDTO.getDescription());
        existing.setDeadline(requestDTO.getDeadline());
        existing.setStatus(requestDTO.getStatus());

        return ProjectMapper.toDTO(projectRepository.save(existing));
    }

    /**
     * Deletes the project with the given ID and all tasks associated with it.
     *
     * @param id the ID of the project to delete
     * @throws ProjectNotFoundException if the project does not exist
     */
    @Auditable(actionType = "DELETE", entityType = "Project")
    @CacheEvict(value = "tasks", key = "#userId")
    @Transactional
    public void deleteProject(Long id) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));

        taskRepository.deleteByProjectId(id);
        projectRepository.delete(existing);
    }

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return the authenticated {@link User}
     * @throws RuntimeException if no authenticated user is found
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }
        throw new RuntimeException("User not authenticated");
    }
}
