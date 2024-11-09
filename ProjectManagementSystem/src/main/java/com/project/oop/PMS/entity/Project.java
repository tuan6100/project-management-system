package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // Relationship with resources (each project can have multiple resources)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Resource> resources;

    // Relationship to link a User as the manager of the project
    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "userID")
    private User manager;

    // Relationship for members of the project
    @ManyToMany
    @JoinTable(
        name = "member_project",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;

    // Constructors
    public Project() {}

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
