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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .password("Not available")
                            .email(email)
                            .roles(Roles.ROLE_CONTRACTOR)
                            .build();
                    return userRepository.save(newUser);
                });

        List<GrantedAuthority> grantedAuthority =
                List.of(new SimpleGrantedAuthority(user.getRoles().toString()));

        return new DefaultOidcUser(grantedAuthority, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
