package com.example.project_tracker.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class DeveloperRequestDTO {

    @NotBlank(message = "name must not be blank")
    private String name;

    @Email
    @NotBlank(message = "email must not be blank")
    private String email;

    @NotEmpty(message = "At least one skill is required")
    private Set<String> skills;

    public DeveloperRequestDTO() {}

    public DeveloperRequestDTO(String name, String email, Set<String> skills) {
        this.name = name;
        this.email = email;
        this.skills = skills;
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

    public static class Builder {
        private String name;
        private String email;
        private Set<String> skills;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder skills(Set<String> skills) {
            this.skills = skills;
            return this;
        }

        public DeveloperRequestDTO build() {
            return new DeveloperRequestDTO(name, email, skills);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
