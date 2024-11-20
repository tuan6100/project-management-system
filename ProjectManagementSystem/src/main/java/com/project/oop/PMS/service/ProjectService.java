package com.project.oop.PMS.service;

import com.project.oop.PMS.dto.ProjectRequest;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    @Lazy
    private UserService userService;

    public Project createProject(ProjectRequest projectRequest) {
        Project project = new Project(projectRequest.getName(), projectRequest.getDescription());
        User manager = userService.getUserById(projectRequest.getManagerId());
        project.setManager(manager);
        project.getMembers().add(manager);
        return projectRepository.save(project);
    }

    public  List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


    public Project updateProject(Integer projectId, Integer userId, ProjectRequest projectRequest) throws RuntimeException {
        Project project = userService.getProject(projectId, userId);
        if (projectRequest.getName() != null) {
            project.setName(projectRequest.getName());
        }
        if (projectRequest.getDescription() != null) {
            project.setDescription(projectRequest.getDescription());
        }
        return projectRepository.save(project);
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
//                throw new RuntimeRuntimeException("User is not authorized to access this project");
//            }
//        }
//        return Optional.empty(); // Project không tồn tại
//    }

    public Project getProjectById(Integer projectId) throws RuntimeException {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public User getManagerByProjectId(Integer projectId) throws RuntimeException {
        Project project = getProjectById(projectId);
        return project.getManager();
    }

    public List<User> getMembersByProjectId(Integer projectId) throws RuntimeException {
        Project project = getProjectById(projectId);
        return project.getMembers();
    }

    public Project addMember(Integer projectId, Integer managerId, List<Integer> usersId) throws RuntimeException {
        Project project = getProjectById(projectId);
        if (!project.getManager().getUserId().equals(managerId)) {
            throw new RuntimeException("You do not have permission to do");
        }
        List<String> errors = new ArrayList<>();
        usersId.forEach(userId -> {
            User user = userService.getUserById(userId);
            if (!project.getMembers().contains(user)) {
                project.getMembers().add(user);
            } else {
                errors.add("User " + user.getUsername() + " is already a member of the project");
            }
        });
        if (!errors.isEmpty()) {
            projectRepository.save(project);
            throw new RuntimeException(String.join("; ", errors));
        }
        return projectRepository.save(project);
    }

    public void removeMember(Integer projectId, Integer managerId, Integer userId) throws RuntimeException {
        if (userId.equals(managerId)) {
            throw new RuntimeException("You cannot remove yourself from the project");
        }
        Project project = getProjectById(projectId);
        if (!project.getManager().getUserId().equals(managerId)) {
            throw new RuntimeException("You do not have permission to do");
        }
        User user = userService.getUserById(userId);
        project.getMembers().remove(user);
        projectRepository.save(project);
    }

    public void deleteProject(Integer projectId, Integer managerId) throws RuntimeException {
        Project project = getProjectById(projectId);
        if (!project.getManager().getUserId().equals(managerId)) {
            throw new RuntimeException("You do not have permission to do");
        }
        projectRepository.delete(project);
    }

}
