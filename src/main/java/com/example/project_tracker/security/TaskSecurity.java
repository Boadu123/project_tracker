package com.example.project_tracker.security;

import com.example.project_tracker.models.Task;
import com.example.project_tracker.repository.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Security component used to determine if the currently authenticated user
 * has permission to access or modify a specific task.
 * <p>
 * This is typically used in Spring Security method-level authorization via SpEL expressions.
 */
@Component("taskSecurity")
public class TaskSecurity {

    private final TaskRepository taskRepository;

    /**
     * Constructs the TaskSecurity component with the required TaskRepository.
     *
     * @param taskRepository the repository for accessing task data
     */
    public TaskSecurity(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Checks whether the currently authenticated user is the owner of the specified task.
     *
     * @param taskId the ID of the task to verify ownership
     * @return {@code true} if the user owns the task; {@code false} otherwise
     */
    public boolean isOwner(Long taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long currentUserId = userDetails.getUser().getId();

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        return taskOpt.map(task -> task.getUser().getId().equals(currentUserId)).orElse(false);
    }
}
