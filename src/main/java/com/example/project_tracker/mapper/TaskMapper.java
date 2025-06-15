package com.example.project_tracker.mapper;

import com.example.project_tracker.DTO.request.TaskRequestDTO;
import com.example.project_tracker.DTO.response.TaskResponseDTO;
import com.example.project_tracker.models.User;
import com.example.project_tracker.models.Project;
import com.example.project_tracker.models.Task;

public class TaskMapper {

    public static Task toEntity(TaskRequestDTO dto, User developer, Project project) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());

        task.setUser(developer);
        task.setProject(project);

        return task;
    }

    public static TaskResponseDTO toDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());

        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
        }

        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
        }

        return dto;
    }
}
