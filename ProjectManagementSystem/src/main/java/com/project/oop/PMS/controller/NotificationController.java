package com.project.oop.PMS.controller;

import com.project.oop.PMS.entity.Notification;
import com.project.oop.PMS.exception.CodeException;
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

    // Tạo thông báo mời tham gia dự án
    @PostMapping("/invite/{userId}/{projectId}/{message}")
    public Notification createProjectInvitation(
            @PathVariable Integer userId,
            @PathVariable Integer projectId,
            @PathVariable String message) {
        return notificationService.createProjectInvitation(userId, projectId, message);
    }

    // Xử lý hành động với thông báo (Accept/Deny)
    @PostMapping("/{notificationId}/action/{action}")
    public String handleNotificationAction(
            @PathVariable Integer notificationId,
            @PathVariable String action) throws CodeException {
        return notificationService.handleNotificationAction(notificationId, action);
    }

    // Lấy danh sách thông báo cho một user
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
}
