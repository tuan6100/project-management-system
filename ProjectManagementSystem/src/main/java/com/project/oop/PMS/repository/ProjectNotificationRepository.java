package com.project.oop.PMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oop.PMS.entity.ProjectNotification;

public interface ProjectNotificationRepository extends JpaRepository<ProjectNotification, Integer> {}