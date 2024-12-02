package com.project.oop.PMS.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "member_task")
public class MemberTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberTaskId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "task_id")
    @JsonBackReference("task-memberTask")
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-memberTask")
    private User member;

    @Column(columnDefinition = "boolean default false")
    private Boolean isCompleted;

    @Column(name = "completed_date")
    private Date completedDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_project_id")
    @JsonBackReference("memberProject-memberTask")
    private MemberProject memberProject;


    public MemberTask() {}

    public MemberTask(Task task, User member, Boolean isCompleted) {
        this.task = task;
        this.member = member;
        this.isCompleted = isCompleted;
    }
}
