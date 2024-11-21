package com.project.oop.PMS.service;

import com.project.oop.PMS.dto.ProjectRequest;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
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

    public Project createProject(ProjectRequest projectRequest) throws CodeException {
        Project project = new Project(projectRequest.getName(), projectRequest.getDescription());
        User manager = userService.getUserById(projectRequest.getManagerId());
        project.setManager(manager);
        project.getMembers().add(manager);
        return projectRepository.save(project);
    }

    public  List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


    public Project updateProject(Integer projectId, Integer userId, ProjectRequest projectRequest) throws CodeException {
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
//                throw new RuntimeCodeException("User is not authorized to access this project");
//            }
//        }
//        return Optional.empty(); // Project không tồn tại
//    }

    public Project getProjectById(Integer projectId) throws CodeException {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CodeException("Project not found"));
    }

    public User getManagerByProjectId(Integer projectId) throws CodeException {
        Project project = getProjectById(projectId);
        return project.getManager();
    }

    public List<User> getMembersByProjectId(Integer projectId) throws CodeException {
        Project project = getProjectById(projectId);
        return project.getMembers();
    }

    public Project addMember(Integer projectId, Integer managerId, List<Integer> usersId) throws CodeException {
        Project project = getProjectById(projectId);
        if (!project.getManager().getUserId().equals(managerId)) {
            throw new CodeException("You do not have permission to do");
        }
        List<String> errors = new ArrayList<>();
        usersId.forEach(userId -> {
            User user;
            try {
                user = userService.getUserById(userId);
                if (!project.getMembers().contains(user)) {
                    project.getMembers().add(user);
                } else {
                    assert user != null;
                    errors.add("User " + user.getUsername() + " is already a member of the project");
                }
            } catch (CodeException e) {
                errors.add(e.getMessage());
            }

        });
        if (!errors.isEmpty()) {
            projectRepository.save(project);
            throw new CodeException(String.join("; ", errors));
        }
        return projectRepository.save(project);
    }

    public void removeMember(Integer projectId, Integer managerId, Integer memberId) throws CodeException {
        if (memberId.equals(managerId)) {
            throw new CodeException("You cannot remove yourself from the project");
        }
        Project project = getProjectById(projectId);
        if (!project.getManager().getUserId().equals(managerId)) {
            throw new CodeException("You do not have permission to do");
        }
        User member = userService.getUserById(memberId);
        if (!project.getMembers().contains(member)) {
            throw new CodeException("User " + member.getUsername() + " is not a member of the project");
        }
        project.getMembers().remove(member);
        projectRepository.save(project);
    }

    public void deleteProject(Integer projectId, Integer managerId) throws CodeException {
        Project project = getProjectById(projectId);
        if (!project.getManager().getUserId().equals(managerId)) {
            throw new CodeException("You do not have permission to do");
        }
        projectRepository.delete(project);
    }

}
