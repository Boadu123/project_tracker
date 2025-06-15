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
import com.example.project_tracker.service.interfaces.TaskServiceInterface;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       ProjectRepository projectRepository, AuditLogService auditLogService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
    }

    @Auditable(actionType = "CREATE", entityType = "Task")
    public TaskResponseDTO createTask(@Valid TaskRequestDTO dto) {
        User developer = null;

        if (dto.getUserId() != null) {
            developer = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("Developer with ID " + dto.getUserId() + " not found"));
        }

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + dto.getProjectId() + " not found"));
        Task task = TaskMapper.toEntity(dto, developer, project);
        Task saved = taskRepository.save(task);
        return TaskMapper.toDTO(saved);
    }

    public List<TaskResponseDTO> getAllTasks(String sortBy) {
        List<String> allowedFields = List.of("dueDate", "status", "createdAt");
        if (!allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);

        return taskRepository.findAll(sort).stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        return TaskMapper.toDTO(task);
    }

    @Auditable(actionType = "UPDATE", entityType = "Task")
    public TaskResponseDTO updateTask(Long id, @Valid TaskRequestDTO dto) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("Developer with ID " + dto.getUserId() + " not found"));
        }

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + dto.getProjectId() + " not found"));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setDueDate(dto.getDueDate());
        existing.setUser(user);
        existing.setProject(project);

        Task updated = taskRepository.save(existing);
        return TaskMapper.toDTO(updated);
    }

    @Auditable(actionType = "DELETE", entityType = "Task")
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        taskRepository.delete(task);
    }

    public List<TaskResponseDTO> getTasksByProjectId(Long projectId) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " not found"));

        return taskRepository.findByProjectId(projectId).stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndStatusNot(LocalDate.now(), "COMPLETED")
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Project> getProjectsWithoutTasks() {
        return projectRepository.findProjectsWithoutTasks();
    }

    public List<TaskRepository.TaskStatusCountProjection> getTaskCountsByStatus() {
        return taskRepository.getTaskCountGroupedByStatus();
    }

}
