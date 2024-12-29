package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.ErrorResponse;
import com.project.oop.PMS.entity.Notification;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.UserRepository;
import com.project.oop.PMS.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;
    

    @PostMapping("/invite/{userName}/{managerId}/{projectId}/{message}")
    public Notification createProjectInvitation(
            @PathVariable String userName,
            @PathVariable Integer managerId,
            @PathVariable Integer projectId,
            @PathVariable String message
    ) {
        return notificationService.createProjectInvitation(userRepository.findByUserName(userName).getUserId(), projectId, message,managerId);
    }

    @PostMapping("/{notificationId}/action/{action}")
    public String handleNotificationAction(
            @PathVariable Integer notificationId,
            @PathVariable String action) throws CodeException {
        return notificationService.handleNotificationAction(notificationId, action);
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUser(@PathVariable Integer userId) {
        return notificationService.getAllNotificationsForUser(userId);
    }
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Integer notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok("Notification marked as read successfully");
        } catch (CodeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/projects/{projectId}/{managerId}/notify-upcoming-tasks/{days}")
    public ResponseEntity<?> notifyUpcomingTasks(
            @PathVariable Integer projectId,
            @PathVariable Integer managerId,
            @PathVariable int days) {
        try {
            notificationService.notifyUpcomingTasks(projectId, managerId, days);
            return ResponseEntity.ok("Notifications sent for upcoming tasks");
        } catch (CodeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(e.getMessage())
            );
        }
    }

}

