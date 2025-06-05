package com.example.project_tracker.mapper;

import com.example.project_tracker.DTO.request.DeveloperRequestDTO;
import com.example.project_tracker.DTO.response.DeveloperResponseDTO;
import com.example.project_tracker.models.Developer;

public class DeveloperMapper {

    public static Developer toEntity(DeveloperRequestDTO dto) {
        Developer developer = new Developer();
        developer.setName(dto.getName());
        developer.setEmail(dto.getEmail());
        developer.setSkills(dto.getSkills());
        return developer;
    }

    public static DeveloperResponseDTO toDTO(Developer developer) {
        DeveloperResponseDTO responseDTO = new DeveloperResponseDTO();
        responseDTO.setId(developer.getId());
        responseDTO.setName(developer.getName());
        responseDTO.setEmail(developer.getEmail());
        responseDTO.setSkills(developer.getSkills());
        return responseDTO;
    }
}
