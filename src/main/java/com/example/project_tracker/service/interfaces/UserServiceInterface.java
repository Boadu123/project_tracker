package com.example.project_tracker.service.interfaces;

import com.example.project_tracker.DTO.request.UserRequestDTO;
import com.example.project_tracker.DTO.response.UserResponseDTO;

public interface UserServiceInterface {
    void registerUser(UserRequestDTO requestDTO);
    UserResponseDTO updateUserDetails(UserRequestDTO requestDTO);
    void deleteLoggedInUser();
    void deleteUserById(Long userId);
}
