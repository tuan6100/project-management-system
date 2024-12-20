package com.project.oop.PMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oop.PMS.entity.TaskNotification;



public interface TaskNotificationRepository extends JpaRepository<TaskNotification, Integer> {}
