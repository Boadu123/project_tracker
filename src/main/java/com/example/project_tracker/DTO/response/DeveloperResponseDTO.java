package com.example.project_tracker.DTO.response;

import java.util.Set;

public class DeveloperResponseDTO {

    private Long id;

    private String name;

    private String email;

    private Set<String> skills;

    public DeveloperResponseDTO() {
    }

    public DeveloperResponseDTO(Long id, String name, String email, Set<String> skills) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }
}
