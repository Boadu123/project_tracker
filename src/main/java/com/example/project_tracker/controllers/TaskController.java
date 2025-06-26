package com.example.project_tracker.controllers;

import com.example.project_tracker.DTO.request.TaskRequestDTO;
import com.example.project_tracker.DTO.response.TaskResponseDTO;
import com.example.project_tracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.project_tracker.utils.SucessResponseUtil.sucessResponseUtil;

/**
 * TaskController handles task creation, retrieval, update, and deletion.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /** Creates a new task. */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTask(@Valid @RequestBody TaskRequestDTO requestDTO) {
        TaskResponseDTO response = taskService.createTask(requestDTO);
        return sucessResponseUtil(HttpStatus.CREATED, response);
    }

    /** Retrieves all tasks sorted by a given field. */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTasks(@RequestParam(defaultValue = "dueDate") String sortBy) {
        List<TaskResponseDTO> response = taskService.getAllTasks(sortBy);
        return sucessResponseUtil(HttpStatus.OK, response);
    }

    /** Retrieves a task by its ID. */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTaskById(@PathVariable Long id) {
        TaskResponseDTO response = taskService.getTaskById(id);
        return sucessResponseUtil(HttpStatus.OK, response);
    }

    /** Updates a task if the user is the owner and has developer role. */
    @PreAuthorize("hasRole('DEVELOPER') and @taskSecurity.isOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTask(@PathVariable Long id,
                                                          @Valid @RequestBody TaskRequestDTO requestDTO) {
        TaskResponseDTO response = taskService.updateTask(id, requestDTO);
        return sucessResponseUtil(HttpStatus.OK, response);
    }

    /** Deletes a task by its ID. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK.value());
        response.put("status", "success");
        response.put("message", "Task deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /** Retrieves all tasks assigned to a specific project. */
    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<Map<String, Object>> getTasksByProjectId(@PathVariable Long projectId) {
        List<TaskResponseDTO> response = taskService.getTasksByProjectId(projectId);
        return sucessResponseUtil(HttpStatus.OK, response);
    }


}
