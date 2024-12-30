package com.project.oop.PMS.service.implement;

import com.project.oop.PMS.dto.ProjectResponse;
import com.project.oop.PMS.dto.ProjectResponseForGetAll;
import com.project.oop.PMS.dto.TaskResponse;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.MemberProjectRepository;
import com.project.oop.PMS.repository.MemberTaskRepository;
import com.project.oop.PMS.repository.UserRepository;
import com.project.oop.PMS.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberProjectRepository memberProjectRepository;

    @Autowired
    @Lazy
    private ProjectServiceImplement projectService;

    @Autowired
    private MemberTaskRepository memberTaskRepository;


    @Override
    public User getUserById(Integer userId) throws CodeException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CodeException("User not found"));
    }

    @Override
    public User getUserByAuth(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);

    }

    @Override
    public User register(String username, String password) throws CodeException {
        if (getUserByAuth(username, password) != null) {
            throw new CodeException ("Username already exist!");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) throws CodeException {
        User user =  getUserByAuth(username, password);
        if (user == null) {
            throw new CodeException("Invalid username or password");
        }
        return user;
    }

    @Override
    public List<ProjectResponseForGetAll> getAllProjects(Integer userId) {
        List<Project> memberProjects = memberProjectRepository.findProjectsByUserId(userId);
        List<ProjectResponseForGetAll> projectResponses = new ArrayList<>();
        memberProjects.forEach(project -> projectResponses.add(projectService.getProjectResponseForGetAll(project)));
        return projectResponses;
    }

    @Override
    public List<TaskResponse> getAllTasksByUser(Integer userId) {
        List<Task> tasks = memberTaskRepository.getTasksByUserId(userId);
        List<TaskResponse> taskResponses = new ArrayList<>();
        tasks.forEach(task -> taskResponses.add(TaskResponse.fromEntity(task)));
        return taskResponses;
    }


    public List<TaskResponse> getTasksCompletedByUser(Integer userId) throws CodeException {
        List<Task> tasks = memberTaskRepository.getTasksCompletedByUser(userId);
        List<TaskResponse> taskResponses = new ArrayList<>();
        tasks.forEach(task -> taskResponses.add(TaskResponse.fromEntity(task)));
        return taskResponses;
    }
    @Override
    public Integer getUserIdByUsername(String username) {
        User user = userRepository.findByUserName(username);
        return user != null ? user.getUserId() : null;
    }

}
