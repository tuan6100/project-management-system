package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "taskId", nullable = false)
    private Task task;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    // Getters and Setters
}
