package com.project.oop.PMS.service;

import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project addProject(String name, String description, User manager) {
        Project project = new Project(name, description);
        project.setManager(manager);
        project.getMembers().add(manager);
        return projectRepository.save(project);
    }

    public  List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


    public Optional<Project> updateProject(Integer projectId, String name, String description, User user) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            if (project.getManager().getUserID().equals(user.getUserID())) {
                project.setName(name);
                project.setDescription(description);
                projectRepository.save(project);
                return Optional.of(project);
            } else {
                throw new RuntimeException("User is not authorized to update this project");
            }
        }
        return Optional.empty();
    }
    
//    public Optional<Project> getProjectById(Integer projectId, User user) {
//        Optional<Project> projectOpt = projectRepository.findById(projectId);
//        if (projectOpt.isPresent()) {
//            Project project = projectOpt.get();
//            // Kiểm tra nếu user là manager hoặc member của project
//            if (project.getManager().getUserID().equals(user.getUserID()) ||
//                project.getMembers().stream().anyMatch(member -> member.getUserID().equals(user.getUserID()))) {
//                return Optional.of(project);
//            } else {
//                throw new RuntimeException("User is not authorized to access this project");
//            }
//        }
//        return Optional.empty(); // Project không tồn tại
//    }

    public Project getProjectById(Integer projectId) throws Exception {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new Exception("Project not found"));
    }

    public User getManagerByProjectId(Integer projectId) throws Exception {
        Project project = getProjectById(projectId);
        return project.getManager();
    }

    public List<User> getMembersByProjectId(Integer projectId) throws Exception {
        Project project = getProjectById(projectId);
        return project.getMembers();
    }

    public Project addMember(Integer projectId, Integer userId, Integer managerId) throws Exception {
        Project project = getProjectById(projectId);
        User user = getManagerByProjectId(projectId);
        if (project.getManager().getUserID().equals(user.getUserID())) {
            throw new RuntimeException("User is not authorized to update this project");
        }
        project.getMembers().add(user);
        return projectRepository.save(project);
    }

    public Project removeMember(Integer projectId, Integer userId, Integer managerId) throws Exception {
        Project project = getProjectById(projectId);
        User user = getManagerByProjectId(projectId);
        if (project.getManager().getUserID().equals(user.getUserID())) {
            throw new RuntimeException("User is not authorized to update this project");
        }
        project.getMembers().remove(user);
        return projectRepository.save(project);
    }

    public boolean deleteProjectByName(String projectName, User user) {
        Optional<Project> projectOpt = projectRepository.findByName(projectName);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            // Kiểm tra nếu user là manager của project
            if (project.getManager().getUserID().equals(user.getUserID())) {
                projectRepository.delete(project);
                return true;
            }
        }
        return false;
    }

}
