package com.project.oop.PMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.Date;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", columnDefinition="TEXT")
    @Lob
    private String content;

    @Column(name = "create_date")
    private Date commentDate;


    public Comment() {
    }

    public Comment(Task task, User user, String content, Date commentDate) {
        this.task = task;
        this.user = user;
        this.content = content;
        this.commentDate = commentDate;
    }

}