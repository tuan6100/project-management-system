package com.project.oop.PMS.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_notifications")
public class TaskNotification extends Notification {

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "task_title", nullable = false)
    private String taskTitle;

    // Constructor đầy đủ
    public TaskNotification(Integer userId, Integer taskId, String taskTitle, String message, String actionType, Integer referenceId) {
        this.setUserId(userId); // Thuộc tính từ lớp cha
        this.setMessage(message); // Thuộc tính từ lớp cha
        this.setActionType(actionType); // Thuộc tính từ lớp cha
        this.setReferenceId(referenceId); // Thuộc tính từ lớp cha
        this.taskId = taskId; // Thuộc tính riêng của TaskNotification
        this.taskTitle = taskTitle; // Thuộc tính mới
    }

    public TaskNotification() {}

    // Getter và Setter cho taskId
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    // Getter và Setter cho taskTitle
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
}
