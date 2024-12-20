package com.project.oop.PMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.oop.PMS.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	;
 // Lấy tất cả thông báo theo userId
    @Query("SELECT n FROM Notification n WHERE n.userId = ?1")
    List<Notification> findAllByUserId(Integer userId);
}
