package com.example.project_tracker.controllers;

import com.example.project_tracker.DTO.request.ProjectRequestDTO;
import com.example.project_tracker.DTO.response.ProjectResponseDTO;
import static com.example.project_tracker.utils.SucessResponseUtil.sucessResponseUtil;
import com.example.project_tracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProject(@Valid @RequestBody ProjectRequestDTO requestDTO) {
        ProjectResponseDTO response = projectService.createProject(requestDTO);
        return sucessResponseUtil(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProjects() {
        List<ProjectResponseDTO> response = projectService.getAllProjects();
        return sucessResponseUtil(HttpStatus.OK, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProjectById(@PathVariable Long id) {
        ProjectResponseDTO response = projectService.getProjectById(id);
        return sucessResponseUtil(HttpStatus.OK, response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProject(@PathVariable Long id,
                                                             @Valid @RequestBody ProjectRequestDTO requestDTO) {
        ProjectResponseDTO response = projectService.updateProject(id, requestDTO);
        return sucessResponseUtil(HttpStatus.OK, response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Developer deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
