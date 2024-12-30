package com.project.oop.PMS.repository;

import com.project.oop.PMS.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t WHERE t.project.projectId = :projectId")
    List<Task> findAllByProjectId(@Param("projectId") Integer projectId);
    Task findByTaskId(Integer taskId);
    List<Task> findAllByDueDateBeforeAndIsOverdueNull(Date currentDate);
    @Query("SELECT COUNT(t) FROM Task t " +
    	       "JOIN t.memberTasks mt " +
    	       "WHERE mt.member.userId = :userId " +
    	       "AND t.completeDate >= :startDate " +
    	       "AND t.completeDate < :endDate " +
    	       "AND t.status = 'completed'")
    	Long countCompletedTasksByUserAndDateRange(
    	        @Param("userId") Integer userId,
    	        @Param("startDate") Date startDate,
    	        @Param("endDate") Date endDate);

}
