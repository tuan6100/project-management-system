package com.project.oop.PMS.entity;

import jakarta.persistence.*;

@Entity

@Table(name = "project_notifications")
public class ProjectNotification extends Notification {

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    // Constructor
    public ProjectNotification(Integer userId, Integer projectId, String message) {
        this.setUserId(userId);
        this.projectId = projectId;
        this.setMessage(message);
    }

    public ProjectNotification() {}

    // Getters and Setters
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
