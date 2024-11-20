package com.project.oop.PMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.oop.PMS.dto.ProjectResponse;
import com.project.oop.PMS.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    void deleteByName(String name);

    boolean existsByName(String name);
    
    List<Project> findAll();

	Optional<Project> findByName(String projectName);
	
	 @Query(value = "SELECT p.* FROM projects p " +
             "JOIN member_project mp ON p.id = mp.project_id " +
             "WHERE mp.user_id = :user_id", nativeQuery = true)
	 List<Project> findAllProjectsByUserId(@Param("user_id") int user_id);
}
