package com.project.oop.PMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.oop.PMS.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	;
 // Lấy tất cả thông báo theo userId
    @Query("SELECT n FROM Notification n WHERE n.userId = ?1")
    List<Notification> findAllByUserId(Integer userId);
    // Xóa thông báo theo ID nếu bạn muốn query tuỳ chỉnh
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.id = :id")
    void deleteNotificationById(@Param("id") Integer id);
}
