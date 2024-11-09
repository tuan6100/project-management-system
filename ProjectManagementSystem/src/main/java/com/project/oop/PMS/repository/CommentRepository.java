package com.project.oop.PMS.repository;

import com.project.oop.PMS.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // You can add custom queries here if needed
}
