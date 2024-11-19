package com.project.oop.PMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oop.PMS.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    void deleteByName(String name);

    boolean existsByName(String name);

	Optional<Project> findByName(String projectName);
}
