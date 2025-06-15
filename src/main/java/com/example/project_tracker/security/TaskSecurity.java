package com.example.project_tracker.security;

import com.example.project_tracker.models.Task;
import com.example.project_tracker.repository.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("taskSecurity")
public class TaskSecurity {

    private final TaskRepository taskRepository;

    public TaskSecurity(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean isOwner(Long taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long currentUserId = userDetails.getUser().getId();

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        return taskOpt.map(task -> task.getUser().getId().equals(currentUserId)).orElse(false);
    }
}

