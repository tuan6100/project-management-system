package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

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

    @Column(name = "email")
    private String email;

    // Projects managed by this user
    @OneToMany(mappedBy = "manager")
    private Set<Project> managedProjects;

    // Projects where this user is a member
    @ManyToMany(mappedBy = "members")
    private Set<Project> memberProjects;

    // Tasks where this user is a member
    @ManyToMany(mappedBy = "members")
    private Set<Task> memberTasks;
}
