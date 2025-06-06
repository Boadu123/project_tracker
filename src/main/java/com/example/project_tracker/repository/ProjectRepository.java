package com.example.project_tracker.repository;

import com.example.project_tracker.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE NOT EXISTS " +
            "(SELECT t FROM Task t WHERE t.projectId = p.id)")
    List<Project> findProjectsWithoutTasks();


}
