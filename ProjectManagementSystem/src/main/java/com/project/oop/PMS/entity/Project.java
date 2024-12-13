package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "createDate")
    private Date createDate;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Resource> resources;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonManagedReference("memberProject-project")
    private List<MemberProject> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("project-task")
    private List<Task> tasks = new ArrayList<>();


    public Project() {}

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
