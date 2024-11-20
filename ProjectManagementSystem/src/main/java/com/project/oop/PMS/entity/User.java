package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    // Projects managed by this user
    @OneToMany(mappedBy = "manager")
    @JsonManagedReference
    private Set<Project> managerProjects;

    // Projects where this user is a member
    @ManyToMany(mappedBy = "members")
    private List<Project> memberProjects;

    // Tasks where this user is a member
    @ManyToMany(mappedBy = "members")
    private List<Task> memberTasks;
}
