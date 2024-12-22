package com.project.oop.PMS.service.implement;

import com.project.oop.PMS.entity.MemberTask;
import com.project.oop.PMS.entity.Notification;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.ProjectNotification;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.TaskNotification;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.NotificationRepository;
import com.project.oop.PMS.service.NotificationService;
import com.project.oop.PMS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.oop.PMS.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImplement implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;
 @Autowired
 private ProjectRepository projectRepository;
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
    @Override
    public void notifyUpcomingTasks(Integer projectId, Integer managerId) throws CodeException {
        // Lấy thông tin dự án
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CodeException("Project not found"));

        // Kiểm tra quyền của manager
  //      if (!project.getManager().getUserId().equals(managerId)) {
    //        throw new CodeException("You are not authorized to notify users for this project");
    //    }

        // Lấy danh sách task sắp đến hạn (còn 1 ngày hoặc ít hơn)
        Date now = new Date();
        Date upcomingDate = new Date(now.getTime() + (24 * 60 * 60 * 1000)); // Thêm 1 ngày

        List<Task> upcomingTasks = project.getTasks().stream()
                .filter(task -> task.getDueDate() != null &&
                        !task.getDueDate().before(now) &&
                        task.getDueDate().before(upcomingDate))
                .toList();

        if (upcomingTasks.isEmpty()) {
            throw new CodeException("No upcoming tasks found within 1 day for this project");
        }

        // Gửi thông báo cho từng thành viên liên quan
        for (Task task : upcomingTasks) {
            for (MemberTask memberTask : task.getMemberTasks()) {
                Integer userId = memberTask.getMember().getUserId();
                String message = "Reminder: Task \"" + task.getTitle() + "\" is due on " + task.getDueDate();

                ProjectNotification notification = new ProjectNotification(
                        userId,
                        projectId,
                        message
                );

                notification.setActionType("UPCOMING_TASK");
                notification.setReferenceId(task.getTaskId());
                notificationRepository.save(notification);
            }
        }
    }
}
