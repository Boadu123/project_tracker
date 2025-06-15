package com.example.project_tracker.service.interfaces;

import com.example.project_tracker.DTO.request.TaskRequestDTO;
import com.example.project_tracker.DTO.response.TaskResponseDTO;
import com.example.project_tracker.repository.TaskRepository;
import com.example.project_tracker.models.Project;

import jakarta.validation.Valid;
import java.util.List;

public interface TaskServiceInterface {

    TaskResponseDTO createTask(@Valid TaskRequestDTO dto);

    List<TaskResponseDTO> getAllTasks(String sortBy);

    TaskResponseDTO getTaskById(Long id);

    TaskResponseDTO updateTask(Long id, @Valid TaskRequestDTO dto);

    void deleteTask(Long id);

    List<TaskResponseDTO> getTasksByProjectId(Long projectId);

//    List<TaskResponseDTO> getTasksByDeveloperId(Long developerId);

    List<TaskResponseDTO> getOverdueTasks();

//    List<TaskRepository.TopDeveloperProjection> getTop5DevelopersWithMostTasks();

    List<Project> getProjectsWithoutTasks();

    List<TaskRepository.TaskStatusCountProjection> getTaskCountsByStatus();
}
