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
import java.util.Collections;
import java.util.Comparator;
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
    public Notification createProjectInvitation(Integer userId, Integer projectId, String message, Integer mangerId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setReferenceId(projectId);
        notification.setMessage("You are invited to Project " + projectRepository.findByProjectId(projectId).getName());
        notification.setActionType("PROJECT_INVITATION");
        notification.setActionStatus("PENDING");
        notification.setManagerId(mangerId);
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
        // Sắp xếp theo createDate giảm dần
        Collections.sort(result, new Comparator<Notification>() {
            @Override
            public int compare(Notification n1, Notification n2) {
                return n2.getCreatedAt().compareTo(n1.getCreatedAt()); // Giảm dần
            }
        });

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
    public void notifyUpcomingTasks(Integer projectId, Integer managerId, int days) throws CodeException {
        // Lấy thông tin dự án
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CodeException("Project not found"));

        // Xác định khoảng thời gian
        Date now = new Date();
        Date upcomingDate = new Date(now.getTime() + (long) days * 24 * 60 * 60 * 1000);

        // Lọc danh sách task sắp đến hạn (trong khoảng số ngày được chọn)
        List<Task> upcomingTasks = project.getTasks().stream()
                .filter(task -> task.getDueDate() != null &&
                        !task.getDueDate().before(now) &&
                        task.getDueDate().before(upcomingDate))
                .toList();

        if (upcomingTasks.isEmpty()) {
            throw new CodeException("No upcoming tasks found within " + days + " days for this project");
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
    @Override
    public void notifyManagerMemberAdded(Integer managerId, Integer projectId, String projectName, String userName) {
        // Tạo thông báo
        String message = "User \"" + userName + "\" accepted to the join the project \"" + projectName + "\".";

        ProjectNotification notification = new ProjectNotification(
                managerId,
                projectId,
                message
        );

        notification.setActionType("MEMBER_ADDED");
        notification.setReferenceId(null); // Không cần tham chiếu cụ thể đến thành viên
        notificationRepository.save(notification);
    }
    @Override
    public void notifyManagerTaskCompleted(Integer managerId, Integer taskId, String taskTitle, String memberName) {
        // Tạo thông báo
        String message = "Member \"" + memberName + "\" has completed the task \"" + taskTitle + "\".";

        ProjectNotification notification = new ProjectNotification(
                managerId,
                taskId,
                message
        );

        notification.setActionType("TASK_COMPLETED");
        notification.setReferenceId(taskId); // Liên kết với task
        notificationRepository.save(notification);
    }
}
