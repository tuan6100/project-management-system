package com.project.oop.PMS.service;

import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project addProject(String name, String description) {
        Project project = new Project(name, description);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> updateProject(Long projectId, String name, String description) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            project.setName(name);
            project.setDescription(description);
            projectRepository.save(project);
            return Optional.of(project);
        }
        return Optional.empty();
    }

    public boolean deleteProjectByName(String projectName) {
        if (projectRepository.existsByName(projectName)) {
            projectRepository.deleteByName(projectName);
            return true;
        }
        return false;
    }
}
