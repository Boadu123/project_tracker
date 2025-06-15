package com.example.project_tracker.mapper;

import com.example.project_tracker.DTO.request.UserRequestDTO;
import com.example.project_tracker.DTO.response.UserResponseDTO;
import com.example.project_tracker.models.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .skills(dto.getSkills())
                .roles(dto.getRoles())
                .build();
    }

    public static UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getSkills(),
                user.getRoles()
        );
    }
}
