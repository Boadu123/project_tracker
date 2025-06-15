package com.example.project_tracker.service.interfaces;

import com.example.project_tracker.DTO.request.UserRequestDTO;
import com.example.project_tracker.DTO.response.UserResponseDTO;

import jakarta.validation.Valid;
import java.util.Map;

public interface DeveloperServiceInterface {

    UserResponseDTO createDeveloper(@Valid UserRequestDTO requestDTO);

    Map<String, Object> getAllDevelopers(int page, int size);

    UserResponseDTO getDeveloperById(Long id);

    UserResponseDTO updateDeveloper(Long id, @Valid UserRequestDTO requestDTO);

    void deleteDeveloper(Long id);
}
