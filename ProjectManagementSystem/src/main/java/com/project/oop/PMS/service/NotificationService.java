package com.project.oop.PMS.service;

import com.project.oop.PMS.entity.Notification;
import com.project.oop.PMS.entity.TaskNotification;
import com.project.oop.PMS.exception.CodeException;

import java.util.List;

public interface NotificationService {


    String handleNotificationAction(Integer notificationId, String action) throws CodeException;
    void markAsRead(Integer notificationId) throws CodeException;

	List<Notification> getAllNotificationsForUser(Integer userId);
	public TaskNotification createTaskAssignmentNotification(Integer userId, Integer taskId, String taskTitle, String message);

	

	Notification createProjectInvitation(Integer userId, Integer projectId, String message, Integer mangerId);
	void notifyUpcomingTasks(Integer projectId, Integer managerId, int days) throws CodeException;
}
