package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "project_notifications")
public class ProjectNotification extends Notification {

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    public ProjectNotification() {
        super();
    }

    public ProjectNotification(Integer userId, Integer projectId, String message) {
        super(userId, message);
        this.projectId = projectId;
    }

}
