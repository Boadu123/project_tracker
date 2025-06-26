package com.example.project_tracker.security;

import com.example.project_tracker.enums.Roles;
import com.example.project_tracker.models.User;
import com.example.project_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom implementation of {@link OidcUserService} that integrates with the application's user repository.
 * <p>
 * This service handles users authenticating via OAuth2/OpenID Connect (e.g., Google login).
 * It either registers new users or fetches existing ones from the database,
 * and maps their roles into Spring Security's {@link GrantedAuthority}.
 */
@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends OidcUserService {

    private final UserRepository userRepository;

    /**
     * Loads the authenticated OIDC user. If the user does not exist in the local database,
     * a new user is created with default role {@link Roles#ROLE_CONTRACTOR}.
     *
     * @param userRequest the incoming OIDC authentication request
     * @return a {@link DefaultOidcUser} enriched with roles from the system
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // Load user info from the OIDC provider (e.g., Google)
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getEmail();

        User user = null;

        // If the user doesn't exist in the system, register them with default role
        if (!userRepository.existsByEmail(email)) {
            User newUser = User.builder()
                    .password("Not available") // OAuth users won't use password login
                    .email(email)
                    .roles(Roles.ROLE_CONTRACTOR)
                    .build();
            user = userRepository.save(newUser);
        }

        // Assign the user’s role as a granted authority
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(
                List.of(new SimpleGrantedAuthority(user.getRoles().toString()))
        );

        // Return a Spring Security-compatible OIDC user
        return new DefaultOidcUser(
                grantedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo()
        );
    }
}
