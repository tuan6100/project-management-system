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
    protected Integer userId;

    @Column(name = "username", nullable = false, unique = true)
    protected String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonManagedReference("memberProject-user")
    private List<MemberProject> memberProjects = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonManagedReference("user-memberTask")
    private List<MemberTask> memberTasks = new ArrayList<>();

}
