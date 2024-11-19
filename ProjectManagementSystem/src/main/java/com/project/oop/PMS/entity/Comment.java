package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentID;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "taskID", nullable = false)
    private Task task;

    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    // Getters and Setters
}
