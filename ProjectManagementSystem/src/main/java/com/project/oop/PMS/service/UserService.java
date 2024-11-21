package com.project.oop.PMS.service;

import com.project.oop.PMS.dto.ProjectResponse;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private ProjectService projectService;
    
    public User registerUser(String username, String password) throws CodeException {
        if (userRepository.findByUsernameAndPassword(username, password).isPresent()) {
            throw new CodeException ("Username đã tồn tại!");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    public User getUserById(Integer userId) throws CodeException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CodeException("User not found"));
    }

    public Project getProject(Integer projectId, Integer userId) throws CodeException{
        return getUserById(userId).getMemberProjects().stream()
                .filter(project -> project.getProjectId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new CodeException("Project not found"));
    }

    public List<ProjectResponse> getAllProjectsByUser(Integer userId) throws CodeException{
        User user = getUserById(userId);
        List<Project> projects = user.getMemberProjects();
        List<ProjectResponse> projectReponses = new ArrayList<>();
        projects.forEach(project -> {
            projectReponses.add(ProjectResponse.fromEntity(project));
        });
        return projectReponses;
    }
}
