package com.project.oop.PMS.service;

import com.project.oop.PMS.entity.Notification;
import com.project.oop.PMS.entity.TaskNotification;
import com.project.oop.PMS.exception.CodeException;

import java.util.List;

public interface NotificationService {

    Notification createProjectInvitation(Integer userId, Integer projectId, String message);

    String handleNotificationAction(Integer notificationId, String action) throws CodeException;

   

	List<Notification> getAllNotificationsForUser(Integer userId);
	public TaskNotification createTaskAssignmentNotification(Integer userId, Integer taskId, String taskTitle, String message);
}
