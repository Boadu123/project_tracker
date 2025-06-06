package com.example.project_tracker.service;

import com.example.project_tracker.DTO.request.TaskRequestDTO;
import com.example.project_tracker.DTO.response.TaskResponseDTO;
import com.example.project_tracker.exceptions.DeveloperNotFoundException;
import com.example.project_tracker.exceptions.ProjectNotFoundException;
import com.example.project_tracker.exceptions.TaskNotFoundException;
import com.example.project_tracker.mapper.TaskMapper;
import com.example.project_tracker.models.Developer;
import com.example.project_tracker.models.Project;
import com.example.project_tracker.models.Task;
import com.example.project_tracker.repository.DeveloperRepository;
import com.example.project_tracker.repository.ProjectRepository;
import com.example.project_tracker.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final DeveloperRepository developerRepository;
    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    public TaskService(TaskRepository taskRepository,
                       DeveloperRepository developerRepository,
                       ProjectRepository projectRepository, AuditLogService auditLogService) {
        this.taskRepository = taskRepository;
        this.developerRepository = developerRepository;
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
    }

    public TaskResponseDTO createTask(@Valid TaskRequestDTO dto) {
        Developer developer = null;

        if (dto.getDeveloperId() != null) {
            developer = developerRepository.findById(dto.getDeveloperId())
                    .orElseThrow(() -> new DeveloperNotFoundException("Developer with ID " + dto.getDeveloperId() + " not found"));
        }

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + dto.getProjectId() + " not found"));
        Task task = TaskMapper.toEntity(dto, developer, project);
        Task saved = taskRepository.save(task);
        auditLogService.logAction("CREATE", "Task", saved.getId().toString(), saved.getDeveloperId().toString(), saved);
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

    public TaskResponseDTO updateTask(Long id, @Valid TaskRequestDTO dto) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        Developer developer = null;
        if (dto.getDeveloperId() != null) {
            developer = developerRepository.findById(dto.getDeveloperId())
                    .orElseThrow(() -> new DeveloperNotFoundException("Developer with ID " + dto.getDeveloperId() + " not found"));
        }

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + dto.getProjectId() + " not found"));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setDueDate(dto.getDueDate());
        existing.setDeveloperId(dto.getDeveloperId());
        existing.setProjectId(dto.getProjectId());

        Task updated = taskRepository.save(existing);
        auditLogService.logAction("UPDATE", "Task", updated.getId().toString(), updated.getDeveloperId().toString(), updated);

        return TaskMapper.toDTO(updated);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        auditLogService.logAction("DELETE", "Task", task.getId().toString(), task.getDeveloperId().toString(), task);
        taskRepository.delete(task);
    }

    public List<TaskResponseDTO> getTasksByProjectId(Long projectId) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " not found"));

        return taskRepository.findByProjectId(projectId).stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getTasksByDeveloperId(Long developerId) {
        developerRepository.findById(developerId)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer with ID " + developerId + " not found"));

        return taskRepository.findByDeveloperId(developerId).stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndStatusNot(LocalDate.now(), "COMPLETED")
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskRepository.TopDeveloperProjection> getTop5DevelopersWithMostTasks() {
        return taskRepository.findTop5Developers(PageRequest.of(0, 5));
    }

    public List<Project> getProjectsWithoutTasks() {
        return projectRepository.findProjectsWithoutTasks();
    }

    public List<TaskRepository.TaskStatusCountProjection> getTaskCountsByStatus() {
        return taskRepository.getTaskCountGroupedByStatus();
    }

}
