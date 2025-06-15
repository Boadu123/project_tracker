package com.example.project_tracker.models;

import com.example.project_tracker.enums.Roles;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> skills;

    @Enumerated(EnumType.STRING)
    private Roles roles;


    public User() {}

    public User(Long id, String name, String email, String password, Set<String> skills, Roles roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.skills = skills;
        this.password = password;
        this.roles = roles;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public static class Builder {
        private String name;
        private String email;
        private String password;
        private Set<String> skills;
        private Roles roles;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder skills(Set<String> skills) {
            this.skills = skills;
            return this;
        }

        public Builder roles(Roles roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            User user = new User();
            user.setName(this.name);
            user.setEmail(this.email);
            user.setSkills(this.skills);
            user.setPassword(this.password);
            user.setRoles(this.roles);
            return user;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
