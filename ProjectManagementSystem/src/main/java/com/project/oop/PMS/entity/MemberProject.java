package com.project.oop.PMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@Entity
@Table(name = "member_project")
public class MemberProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer member_project_id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-memberProject")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "project_id")
    @JsonBackReference("project-memberProject")
    private Project project;

    @Column(name = "role", updatable = false)
    private String role;

    @Column(name = "join_date", insertable = false, updatable = false)
    private Date joinDate;

    @OneToMany(mappedBy = "memberProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("memberTask-memberProject")
    private List<MemberTask> memberTasks = new ArrayList<>();


    public MemberProject() {}

    public MemberProject(User user, Project project) {
        this.user = user;
        this.project = project;
    }

    public MemberProject(User user, Project project, String role) {
        this.user = user;
        this.project = project;
        this.role = role;
    }
}
