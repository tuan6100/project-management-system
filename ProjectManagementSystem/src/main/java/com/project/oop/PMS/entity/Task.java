package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskID;

    private Integer projectID;
    private String title;

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    // Relationship with comments (each task can have multiple comments)
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    // Relationship for members assigned to this task
    @ManyToMany
    @JoinTable(
        name = "member_task",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;
}
