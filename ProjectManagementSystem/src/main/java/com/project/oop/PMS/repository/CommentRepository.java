package com.project.oop.PMS.repository;

import com.project.oop.PMS.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c WHERE c.task.taskId = ?1")
    List<Comment> findByTaskId(Integer taskId);

    @Query("SELECT c FROM Comment c WHERE c.task.taskId = ?1 AND c.user.userId = ?2")
    Comment findByTaskIdAndUserId(Integer taskId, Integer userId);
}