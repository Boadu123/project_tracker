package com.example.project_tracker.service;

import com.example.project_tracker.DTO.request.TaskRequestDTO;
import com.example.project_tracker.DTO.response.TaskResponseDTO;
import com.example.project_tracker.annotations.Auditable;
import com.example.project_tracker.exceptions.UserNotFoundException;
import com.example.project_tracker.exceptions.ProjectNotFoundException;
import com.example.project_tracker.exceptions.TaskNotFoundException;
import com.example.project_tracker.mapper.TaskMapper;
import com.example.project_tracker.models.User;
import com.example.project_tracker.models.Project;
import com.example.project_tracker.models.Task;
import com.example.project_tracker.repository.ProjectRepository;
import com.example.project_tracker.repository.TaskRepository;
import com.example.project_tracker.repository.UserRepository;
import com.example.project_tracker.security.CustomUserDetails;
import com.example.project_tracker.service.interfaces.TaskServiceInterface;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService implements TaskServiceInterface {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    // Make allowed fields static so it's not created on every method call
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("dueDate", "status", "createdAt");

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       ProjectRepository projectRepository,
                       AuditLogService auditLogService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
    }

    @Auditable(actionType = "CREATE", entityType = "Task")
    public TaskResponseDTO createTask(@Valid TaskRequestDTO dto) {
        User developer = (dto.getUserId() != null)
                ? userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + dto.getUserId() + " not found"))
                : null;

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + dto.getProjectId() + " not found"));

        Task task = TaskMapper.toEntity(dto, developer, project);
        return TaskMapper.toDTO(taskRepository.save(task));
    }

    @Auditable(actionType = "GET", entityType = "Task")
    public List<TaskResponseDTO> getAllTasks(String sortBy) {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        return mapTasks(taskRepository.findAll(sort));
    }

    @Auditable(actionType = "GET", entityType = "Task")
    public TaskResponseDTO getTaskById(Long id) {
        return TaskMapper.toDTO(
                taskRepository.findById(id)
                        .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"))
        );
    }

    @Auditable(actionType = "UPDATE", entityType = "Task")
    public TaskResponseDTO updateTask(Long id, @Valid TaskRequestDTO dto) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));

        //  Use helper method to get authenticated user
        User currentUser = getAuthenticatedUser();

        if (existing.getUser() == null || !existing.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this task.");
        }

        User assignedUser = (dto.getUserId() != null)
                ? userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Developer with ID " + dto.getUserId() + " not found"))
                : null;

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + dto.getProjectId() + " not found"));

        // Update task
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setDueDate(dto.getDueDate());
        existing.setUser(assignedUser);
        existing.setProject(project);

        return TaskMapper.toDTO(taskRepository.save(existing));
    }

    @Auditable(actionType = "DELETE", entityType = "Task")
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        taskRepository.delete(task);
    }

    @Auditable(actionType = "GET", entityType = "Task")
    public List<TaskResponseDTO> getTasksByProjectId(Long projectId) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " not found"));

        return mapTasks(taskRepository.findByProjectId(projectId));
    }

    public List<TaskResponseDTO> getOverdueTasks() {
        return mapTasks(taskRepository.findByDueDateBeforeAndStatusNot(LocalDate.now(), "COMPLETED"));
    }

    public List<Project> getProjectsWithoutTasks() {
        return projectRepository.findProjectsWithoutTasks();
    }

    public List<TaskRepository.TaskStatusCountProjection> getTaskCountsByStatus() {
        return taskRepository.getTaskCountGroupedByStatus();
    }

    // Helper to avoid repeating stream mapping logic
    private List<TaskResponseDTO> mapTasks(List<Task> tasks) {
        return tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
    }

    // Centralized authentication to avoid repeated casting
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }
        throw new RuntimeException("User not authenticated");
    }
}
