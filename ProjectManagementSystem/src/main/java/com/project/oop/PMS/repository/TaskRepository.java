package com.project.oop.PMS.repository;

import com.project.oop.PMS.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
