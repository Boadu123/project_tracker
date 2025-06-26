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

/**
 * Service implementation for managing tasks in the project tracker system.
 * Handles business logic for creating, retrieving, updating, and deleting tasks.
 * Integrates with the User, Project, and Task repositories, and supports audit logging.
 *
 * <p>This service ensures proper access control, validation, and entity relationships
 * between tasks, projects, and users (developers).</p>
 *
 * <p>All mutating operations are annotated with {@link com.example.project_tracker.annotations.Auditable}
 * for audit trail generation.</p>
 *
 * @author
 */

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

    /**
     * Creates a new task based on the provided DTO.
     *
     * @param dto the task creation request containing title, description, due date, user ID, and project ID
     * @return the created task as a DTO
     * @throws UserNotFoundException if the specified user does not exist
     * @throws ProjectNotFoundException if the specified project does not exist
     */

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

    /**
     * Retrieves all tasks, sorted by the specified field.
     *
     * @param sortBy the field to sort tasks by (e.g., dueDate, status, createdAt)
     * @return a list of task response DTOs
     * @throws IllegalArgumentException if an unsupported sort field is provided
     */

    @Auditable(actionType = "GET", entityType = "Task")
    public List<TaskResponseDTO> getAllTasks(String sortBy) {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        return mapTasks(taskRepository.findAll(sort));
    }

    /**
     * Retrieves a task by its unique ID.
     *
     * @param id the task ID
     * @return the task as a DTO
     * @throws TaskNotFoundException if the task with the specified ID does not exist
     */

    @Auditable(actionType = "GET", entityType = "Task")
    public TaskResponseDTO getTaskById(Long id) {
        return TaskMapper.toDTO(
                taskRepository.findById(id)
                        .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"))
        );
    }

    /**
     * Updates an existing task with new values from the request DTO.
     *
     * @param id the ID of the task to update
     * @param dto the updated task data
     * @return the updated task as a DTO
     * @throws TaskNotFoundException if the task does not exist
     * @throws UserNotFoundException if the specified user does not exist
     * @throws ProjectNotFoundException if the specified project does not exist
     * @throws AccessDeniedException if the authenticated user is not the owner of the task
     */

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

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     * @throws TaskNotFoundException if the task does not exist
     */

    @Auditable(actionType = "DELETE", entityType = "Task")
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        taskRepository.delete(task);
    }

    /**
     * Retrieves all tasks associated with a specific project.
     *
     * @param projectId the ID of the project
     * @return a list of task DTOs under the specified project
     * @throws ProjectNotFoundException if the project does not exist
     */

    @Auditable(actionType = "GET", entityType = "Task")
    public List<TaskResponseDTO> getTasksByProjectId(Long projectId) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " not found"));

        return mapTasks(taskRepository.findByProjectId(projectId));
    }

    /**
     * Retrieves tasks that are overdue and not marked as completed.
     *
     * @return a list of overdue task DTOs
     */

    public List<TaskResponseDTO> getOverdueTasks() {
        return mapTasks(taskRepository.findByDueDateBeforeAndStatusNot(LocalDate.now(), "COMPLETED"));
    }

    /**
     * Retrieves projects that currently have no associated tasks.
     *
     * @return a list of project entities with no tasks
     */

    public List<Project> getProjectsWithoutTasks() {
        return projectRepository.findProjectsWithoutTasks();
    }

    /**
     * Retrieves the count of tasks grouped by their status (e.g., PENDING, COMPLETED).
     *
     * @return a list of projections containing status and task count
     */

    public List<TaskRepository.TaskStatusCountProjection> getTaskCountsByStatus() {
        return taskRepository.getTaskCountGroupedByStatus();
    }

    /**
     * Maps a list of Task entities to their corresponding DTOs.
     *
     * @param tasks list of Task entities
     * @return list of TaskResponseDTOs
     */

    private List<TaskResponseDTO> mapTasks(List<Task> tasks) {
        return tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all tasks assigned to a specific user.
     *
     * @param userId the ID of the user
     * @return a list of TaskResponseDTOs assigned to the user
     * @throws UserNotFoundException if the user does not exist
     */
    @Auditable(actionType = "GET", entityType = "Task")
    public List<TaskResponseDTO> getTasksByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        return mapTasks(taskRepository.findByUserId(userId));
    }

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return the authenticated User entity
     * @throws RuntimeException if authentication information is missing or invalid
     */

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }
        throw new RuntimeException("User not authenticated");
    }
}
