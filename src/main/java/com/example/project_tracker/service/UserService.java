package com.example.project_tracker.service;

import com.example.project_tracker.DTO.request.UserRequestDTO;
import com.example.project_tracker.DTO.response.UserResponseDTO;
import com.example.project_tracker.annotations.Auditable;
import com.example.project_tracker.exceptions.EmailAlreadyExistsException;
import com.example.project_tracker.models.User;
import com.example.project_tracker.repository.UserRepository;
import com.example.project_tracker.security.CustomUserDetails;
import com.example.project_tracker.service.interfaces.UserServiceInterface;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Auditable(actionType = "CREATE", entityType = "User")
    public void registerUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        User user = User.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .skills(requestDTO.getSkills())
                .roles(requestDTO.getRoles())
                .build();

        userRepository.save(user);
    }

    @Auditable(actionType = "GET", entityType = "User")
    public UserResponseDTO getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = getAuthenticatedUser();
            return UserResponseDTO.fromEntity(user);
        }

        throw new RuntimeException("User not authenticated");
    }

    @Auditable(actionType = "UPDATE", entityType = "User")
    public UserResponseDTO updateUserDetails(UserRequestDTO requestDTO) {
        User user = getAuthenticatedUser();

        user.setName(requestDTO.getName());
        user.setSkills(requestDTO.getSkills());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return UserResponseDTO.fromEntity(updatedUser);
    }

    @Auditable(actionType = "DELETE", entityType = "User")
    public void deleteLoggedInUser() {
        User user = getAuthenticatedUser();
        userRepository.deleteById(user.getId());
    }

    @Auditable(actionType = "GET", entityType = "User")
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserResponseDTO.fromEntity(user))
                .collect(Collectors.toList());
    }

    @Auditable(actionType = "DELETE", entityType = "User")
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User with ID " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUser();
        }
        throw new RuntimeException("User not authenticated");
    }

}
