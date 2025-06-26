package com.example.project_tracker.security;

import com.example.project_tracker.models.User;
import com.example.project_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of {@link UserDetailsService} used by Spring Security
 * to load user-specific data during the authentication process.
 * <p>
 * Retrieves user information by email and wraps it in a {@link CustomUserDetails} object.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by their email address.
     * <p>
     * This method is called automatically by Spring Security during login.
     *
     * @param email the email of the user to retrieve
     * @return a {@link UserDetails} object representing the authenticated user
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}
