package com.project.oop.PMS.service;

import com.project.oop.PMS.dto.ProjectResponse;
import com.project.oop.PMS.dto.TaskResponse;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.MemberProjectRepository;
import com.project.oop.PMS.repository.MemberTaskRepository;
import com.project.oop.PMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberProjectRepository memberProjectRepository;

    @Autowired
    @Lazy
    private ProjectService projectService;

    @Autowired
    private MemberTaskRepository memberTaskRepository;

    public User getUserById(Integer userId) throws CodeException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CodeException("User not found"));
    }

    public User getUserByAuth(String username, String password) throws CodeException {
        User user =  userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            throw new CodeException("User not found");
        }
        return user;
    }
    
    public User register(String username, String password) throws CodeException {
        if (getUserByAuth(username, password) != null) {
            throw new CodeException ("Username already exist!");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public User login(String username, String password) throws CodeException {
        return getUserByAuth(username, password);
    }


    public List<ProjectResponse> getAllProjects(Integer userId) throws CodeException{
        List<Project> memberProjects = memberProjectRepository.findProjectsByUserId(userId);
        List<ProjectResponse> projectResponses = new ArrayList<>();
        memberProjects.forEach(project -> projectResponses.add(projectService.getProjectResponse(project)));
        return projectResponses;
    }

    public List<TaskResponse> getAllTasksByUser(Integer userId) throws CodeException{
        List<Task> tasks = memberTaskRepository.getTasksByUserId(userId);
        List<TaskResponse> taskResponses = new ArrayList<>();
        tasks.forEach(task -> taskResponses.add(TaskResponse.fromEntity(task)));
        return taskResponses;
    }

    public List<TaskResponse> getTasksCompletedByUser(Integer userId) throws CodeException{
        List<Task> tasks = memberTaskRepository.getTasksCompletedByUser(userId);
        List<TaskResponse> taskResponses = new ArrayList<>();
        tasks.forEach(task -> taskResponses.add(TaskResponse.fromEntity(task)));
        return taskResponses;
    }

}
