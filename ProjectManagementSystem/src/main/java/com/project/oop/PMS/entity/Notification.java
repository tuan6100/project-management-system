package com.project.oop.PMS.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "notifications")
@Inheritance(strategy = InheritanceType.JOINED)
public  class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "user_id", nullable = false)
    protected Integer userId;

    @Column(nullable = false)
    protected String message;

    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_read", nullable = false)
    protected Boolean isRead = false;

    @Column(name = "action_status", nullable = false)
    protected String actionStatus = "PENDING";

    @Column(name = "action_type")
    protected String actionType;

    @Column(name = "manager_id")
    protected Integer managerId;

    @Column(name = "reference_id")
    protected Integer referenceId;
    

    public Notification() {
    }

    public Notification(Integer userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Notification(Integer userId, String message, String actionType, Integer referenceId) {
        this.userId = userId;
        this.message = message;
        this.actionType = actionType;
        this.referenceId = referenceId;
    }
}
