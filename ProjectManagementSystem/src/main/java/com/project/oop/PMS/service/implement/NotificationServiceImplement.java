package com.project.oop.PMS.service.implement;

import com.project.oop.PMS.entity.Notification;
import com.project.oop.PMS.entity.ProjectNotification;
import com.project.oop.PMS.entity.TaskNotification;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.NotificationRepository;
import com.project.oop.PMS.service.NotificationService;
import com.project.oop.PMS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImplement implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Override
    public Notification createProjectInvitation(Integer userId, Integer projectId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setReferenceId(projectId);
        notification.setMessage(message);
        notification.setActionType("PROJECT_INVITATION");
        notification.setActionStatus("PENDING");
        return notificationRepository.save(notification);
    }

    @Override
    public String handleNotificationAction(Integer notificationId, String action) throws CodeException {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CodeException("Notification not found"));

        if (!"PENDING".equals(notification.getActionStatus())) {
            throw new CodeException("Action already taken");
        }

        if ("ACCEPT".equalsIgnoreCase(action)) {
            notification.setActionStatus("ACCEPTED");
            // Logic thêm user vào project
        } else if ("DENY".equalsIgnoreCase(action)) {
            notification.setActionStatus("DENIED");
        } else {
            throw new CodeException("Invalid action");
        }

        notificationRepository.save(notification);
        return notification.getActionStatus();
    }

    @Override
    public List<Notification> getAllNotificationsForUser(Integer userId) {
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);
        List<Notification> result = new ArrayList<>();

        for (Notification notification : notifications) {
            if (notification instanceof TaskNotification) {
                TaskNotification taskNotification = (TaskNotification) notification;
                // Thêm logic nếu cần xử lý thêm
                result.add(taskNotification);
            } else if (notification instanceof ProjectNotification) {
                ProjectNotification projectNotification = (ProjectNotification) notification;
                // Thêm logic nếu cần xử lý thêm
                result.add(projectNotification);
            } else {
                // Thêm loại khác nếu cần
                result.add(notification);
            }
        }

        return result;
    }
    public TaskNotification createTaskAssignmentNotification(Integer userId, Integer taskId, String taskTitle, String message) {
        // Tạo một đối tượng TaskNotification với đầy đủ thông tin
        TaskNotification taskNotification = new TaskNotification(
            userId,         // ID của người dùng nhận thông báo
            taskId,         // ID của công việc
            taskTitle,      // Tiêu đề công việc
            message,        // Nội dung thông báo
            "TASK_ASSIGNMENT", // Loại hành động (actionType)
            taskId          // Tham chiếu tới công việc (referenceId)
        );
        // Lưu thông báo vào cơ sở dữ liệu
        return notificationRepository.save(taskNotification);
    }
    @Override
    public void markAsRead(Integer notificationId) throws CodeException {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CodeException("Notification not found"));

        if (notification.getIsRead()) {
            throw new CodeException("Notification is already marked as read");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
    
}
