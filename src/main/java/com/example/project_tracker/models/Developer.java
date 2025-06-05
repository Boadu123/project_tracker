package com.example.project_tracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "developer")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection
    private Set<String> skills;

    @OneToMany(mappedBy = "developerId")
    private Set<Task> tasks;

    public Developer() {}

    public Developer(Long id, String name, String email, Set<String> skills, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.skills = skills;
        this.tasks = tasks;
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

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
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

        public Developer build() {
            Developer developer = new Developer();
            developer.setName(this.name);
            developer.setEmail(this.email);
            developer.setSkills(this.skills);
            return developer;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
