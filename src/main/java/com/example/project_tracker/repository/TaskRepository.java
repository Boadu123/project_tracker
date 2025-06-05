package com.example.project_tracker.repository;

import com.example.project_tracker.models.Developer;
import com.example.project_tracker.models.Project;
import com.example.project_tracker.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByDeveloperId(Long developerId);
}
