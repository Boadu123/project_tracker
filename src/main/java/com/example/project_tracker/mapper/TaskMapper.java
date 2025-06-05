package com.example.project_tracker.mapper;

import com.example.project_tracker.DTO.request.TaskRequestDTO;
import com.example.project_tracker.DTO.response.TaskResponseDTO;
import com.example.project_tracker.models.Developer;
import com.example.project_tracker.models.Project;
import com.example.project_tracker.models.Task;

public class TaskMapper {
    public static Task toEntity(TaskRequestDTO dto, Developer developer, Project project) {
        Task task = new Task();
        task.setDescription(dto.getDescription());
        task.setTitle(dto.getTitle());
        task.setStatus(dto.getStatus());
        task.setDeveloperId(dto.getDeveloperId());
        task.setDueDate(dto.getDueDate());
        task.setProjectId(dto.getProjectId());
        return task;
    }

    public static TaskResponseDTO toDTO(Task task) {
        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setId(task.getId());
        responseDTO.setTitle(task.getTitle());
        responseDTO.setDescription(task.getDescription());
        responseDTO.setStatus(task.getStatus());
        if (task.getDeveloperId() != null) {
            responseDTO.setDeveloperId(task.getDeveloperId());
        }
        responseDTO.setDueDate(task.getDueDate());
        if (task.getProjectId() != null) {
            responseDTO.setProjectId(task.getProjectId());
        }
        return responseDTO;
    }
}

