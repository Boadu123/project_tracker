package com.example.project_tracker.repository;

import com.example.project_tracker.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {}
