package com.example.project_tracker.repository;

import com.example.project_tracker.models.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
//    List<Task> findByDeveloperId(Long developerId);
    List<Task> findAll(Sort sort);
    List<Task> findByUserId(Long userId);
    void deleteByProjectId(Long projectId);
    List<Task> findByDueDateBeforeAndStatusNot(java.time.LocalDate dueDate, String completedStatus);

    @Query("SELECT t.status as status, COUNT(t) as count " +
            "FROM Task t GROUP BY t.status")
    List<TaskStatusCountProjection> getTaskCountGroupedByStatus();

    public interface TaskStatusCountProjection {
        String getStatus();
        Long getCount();
    }


}
