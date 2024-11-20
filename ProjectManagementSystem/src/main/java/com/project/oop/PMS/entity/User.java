package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    // Projects managed by this user
    @OneToMany(mappedBy = "manager")
    @JsonManagedReference
    private List<Project> managerProjects;

    @ManyToMany(mappedBy = "members")
    @ToString.Exclude
    private List<Project> memberProjects = new ArrayList<>();

    // Tasks where this user is a member
    @ManyToMany(mappedBy = "members")
    private Set<Task> memberTasks;
}
