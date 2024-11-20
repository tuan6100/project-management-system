package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    private String name;

    private String description;

    // Relationship with resources (each project can have multiple resources)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Resource> resources;

    // Relationship to link a User as the manager of the project
   
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY) // Cascade giúp tự động lưu User nếu User chưa được lưu
    @JoinColumn(name = "manager_id")
    @JsonBackReference
    private User manager; 

    // Relationship for members of the project
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "member_project",
        joinColumns = @JoinColumn(name = "projectId"),
        inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private List<User> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Task> tasks;


    // Constructors
    public Project() {}

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
