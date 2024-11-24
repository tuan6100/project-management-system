package com.project.oop.PMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String password;


    @OneToMany(mappedBy = "manager")
    private List<Project> managerProjects = new ArrayList<>();

    @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Project> memberProjects = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonManagedReference("user-memberTask")
    private List<MemberTask> memberTasks = new ArrayList<>();


}
