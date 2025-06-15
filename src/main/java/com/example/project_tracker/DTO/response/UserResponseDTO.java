package com.example.project_tracker.DTO.response;

import com.example.project_tracker.enums.Roles;

import java.util.Set;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Set<String> skills;
    private Roles roles;

    public UserResponseDTO(Long id, String name, String email, Set<String> skills, Roles roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.skills = skills;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public Roles getRoles() {
        return roles;
    }

}
