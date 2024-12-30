package com.project.oop.PMS.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "task_notifications")
public class TaskNotification extends Notification {

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "task_title", nullable = false)
    private String taskTitle;


    public TaskNotification() {
        super();
    }

    public TaskNotification(Integer userId, Integer taskId, String taskTitle, String message, String actionType, Integer referenceId) {
        super(userId, message, actionType, referenceId);
        this.taskId = taskId;
        this.taskTitle = taskTitle;
    }


}
