package com.example.project_tracker.controllers;

import com.example.project_tracker.DTO.request.DeveloperRequestDTO;
import com.example.project_tracker.DTO.response.DeveloperResponseDTO;
import com.example.project_tracker.service.DeveloperService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.example.project_tracker.utils.SucessResponseUtil.sucessResponseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {

    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDeveloper(@Valid @RequestBody DeveloperRequestDTO requestDTO) {
        DeveloperResponseDTO response = developerService.createDeveloper(requestDTO);
        return sucessResponseUtil(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDevelopers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        Map<String, Object> response = developerService.getAllDevelopers(page, size);
        return sucessResponseUtil(HttpStatus.OK, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDeveloperById(@PathVariable Long id) {
        DeveloperResponseDTO response = developerService.getDeveloperById(id);
        return sucessResponseUtil(HttpStatus.OK, response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDeveloper(@PathVariable Long id,
                                                               @Valid @RequestBody DeveloperRequestDTO requestDTO) {
        DeveloperResponseDTO response = developerService.updateDeveloper(id, requestDTO);
        return sucessResponseUtil(HttpStatus.OK, response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDeveloper(@PathVariable Long id) {
        developerService.deleteDeveloper(id);
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Developer deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
