package com.project.oop.PMS.repository;

import com.project.oop.PMS.entity.MemberProject;
import com.project.oop.PMS.entity.MemberTask;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberProjectRepository extends JpaRepository<MemberProject, Integer> {

    @Query(" SELECT m FROM MemberProject m WHERE m.user = ?1 AND m.project = ?2 ")
    MemberProject findByUserAndProject(User user, Project project);

    @Query(" SELECT m.user FROM MemberProject m WHERE m.project.projectId = ?1 ")
    List<User> findUserBytProjectId(Integer projectId);

    @Query(" SELECT m.user FROM MemberProject m WHERE m.project.projectId = ?1 AND m.role = 'Manager' ")
    User findManagerIdByProjectId(Integer projectId);

    @Query(" SELECT m.user FROM MemberProject m WHERE m.project.projectId = ?1 AND m.role != 'Manager' ")
    List<User> findMemberNotManagerByProjectId(Integer projectId);

    @Query(" SELECT m.project FROM MemberProject m WHERE m.user.userId = ?1 ")
    List<Project> findProjectsByUserId(Integer userId);

    @Query("SELECT m FROM MemberProject m WHERE m.project.projectId = ?1")
    List<MemberProject> findMemberProjectsByProjectId(Integer projectId);

    @Query("SELECT m FROM MemberProject m WHERE m.user.userId = ?1 AND m.project.projectId = ?2")
    List<MemberProject> findByUserIdAndProjectId(Integer userId, Integer projectId);
}
