package com.project.oop.PMS.repository;

import com.project.oop.PMS.entity.MemberProject;
import com.project.oop.PMS.entity.MemberTask;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberTaskRepository extends JpaRepository<MemberTask, Integer> {

    MemberTask findByMemberAndTask(User member, Task task);

    @Query("SELECT m.member FROM MemberTask m WHERE m.task.taskId = ?1")
    List<User> getMembersByTaskId(Integer taskId);

    @Query("SELECT m.task FROM MemberTask m WHERE m.member.userId = ?1")
    List<Task> getTasksByUserId(Integer memberId);

    @Query("SELECT m.task FROM MemberTask m WHERE m.member.userId = ?1 AND m.is_completed = true")
    List<Task> getTasksCompletedByUser(Integer memberId);

    @Query("SELECT m FROM MemberTask m WHERE m.task.taskId = ?1")
    List<MemberTask> findMemberTaskByTaskId(Integer taskId);

    @Query("SELECT COUNT(m) > 0 FROM MemberTask m WHERE m.task = :task AND m.is_completed = :isCompleted")
    boolean existsByTaskAndIsCompleted(@Param("task") Task task, @Param("isCompleted") boolean isCompleted);}
