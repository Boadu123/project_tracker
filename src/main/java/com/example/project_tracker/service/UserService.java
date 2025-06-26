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

/**
 * Service class responsible for managing user-related operations such as registration,
 * profile updates, deletion, and retrieval of user details.
 * <p>
 * This service uses Spring Security for authentication context access
 * and handles data persistence through the {@link UserRepository}.
 * <p>
 * All mutating actions are logged using the custom {@link Auditable} annotation.
 */
@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs the UserService with required dependencies.
     *
     * @param userRepository  repository for accessing user data
     * @param passwordEncoder encoder for securing user passwords
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user after checking for existing email.
     *
     * @param requestDTO the user registration request data
     * @throws EmailAlreadyExistsException if the provided email already exists
     */
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

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @return the logged-in user's information as a DTO
     * @throws RuntimeException if the user is not authenticated
     */
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

    /**
     * Updates the currently authenticated user's name, skills, and optionally password.
     *
     * @param requestDTO the updated user data
     * @return the updated user details as a DTO
     * @throws RuntimeException if the user is not authenticated
     */
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

    /**
     * Deletes the currently authenticated user from the system.
     *
     * @throws RuntimeException if the user is not authenticated
     */
    @Auditable(actionType = "DELETE", entityType = "User")
    public void deleteLoggedInUser() {
        User user = getAuthenticatedUser();
        userRepository.deleteById(user.getId());
    }

    /**
     * Retrieves a list of all registered users in the system.
     *
     * @return list of user DTOs
     */
    @Auditable(actionType = "GET", entityType = "User")
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserResponseDTO.fromEntity(user))
                .collect(Collectors.toList());
    }

    /**
     * Deletes a user by their ID if they exist in the system.
     *
     * @param userId the ID of the user to delete
     * @throws RuntimeException if the user does not exist
     */
    @Auditable(actionType = "DELETE", entityType = "User")
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User with ID " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    /**
     * Retrieves the authenticated user from the Spring Security context.
     *
     * @return the currently authenticated {@link User}
     * @throws RuntimeException if no authenticated user is found
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUser();
        }
        throw new RuntimeException("User not authenticated");
    }
}
