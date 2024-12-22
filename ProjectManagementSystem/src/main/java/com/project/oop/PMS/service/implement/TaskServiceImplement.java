package com.project.oop.PMS.service.implement;

import com.project.oop.PMS.dto.RateReport;
import com.project.oop.PMS.dto.TaskRequest;
import com.project.oop.PMS.dto.TaskResponseForGetAll;
import com.project.oop.PMS.entity.*;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.MemberProjectRepository;
import com.project.oop.PMS.repository.MemberTaskRepository;
import com.project.oop.PMS.repository.TaskRepository;
import com.project.oop.PMS.repository.UserRepository;
import com.project.oop.PMS.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskServiceImplement implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MemberTaskRepository memberTaskRepository;

    @Autowired
    private MemberProjectRepository memberProjectRepository;

    @Autowired
    @Lazy
    private ProjectServiceImplement projectService;

    @Autowired
    @Lazy
    private UserServiceImplement userService;
    @Autowired
    private UserRepository userRepository;


    public Task getTaskById(Integer taskId) throws CodeException {
        return taskRepository.findById(taskId).orElseThrow(() -> new CodeException("Task not found"));
    }

    public Task createTask(TaskRequest taskRequest, Integer projectId, Integer managerId) throws CodeException {
        if (!projectService.getManager(projectId).getUserId().equals(managerId)) {
            throw new CodeException("You are not the manager of this project");
        }
        if (taskRequest.getDueDate().before(new Date())) {
            throw new CodeException("Due date must be in the future");
        }
        Task task = taskRequest.toTask();
        task.setProject(projectService.getProjectById(projectId));
        return taskRepository.save(task);
    }


    public String assignMember(Integer taskId, Integer memberId, Integer managerId) throws CodeException {
        Task task = getTaskById(taskId);
        Project project = task.getProject();
        if (!projectService.getManager(project.getProjectId()).getUserId().equals(managerId)) {
            throw new CodeException("You are not the manager of this project");
        }
        if ((projectService.getMembers(project.getProjectId()).contains(userService.getUserById(memberId)))) {
            throw new CodeException("User is not a member of this project");
        }
        if (task.getMemberTasks().stream().anyMatch(mt -> mt.getMember().getUserId().equals(memberId))) {
            throw new CodeException("User " + userService.getUserById(memberId).getUsername() + " is already assigned to this task");
        }
        MemberTask memberTask = new MemberTask();
        MemberProject memberProject = memberProjectRepository.findByUserAndProject(userService.getUserById(memberId), project);
        memberTask.setTask(task);
        memberTask.setMember(userService.getUserById(memberId));
        memberTask.setIs_completed(false);
        memberTask.setMemberProject(memberProject);
        memberTaskRepository.save(memberTask);
        task.getMemberTasks().add(memberTask);
        taskRepository.save(task);
        return "Member " + userService.getUserById(memberId).getUsername() + " assigned to task " + task.getTitle();
    }


    public String removeMember(Integer taskId, Integer memberId, Integer managerId) throws CodeException {
        Task task = getTaskById(taskId);
        if (!projectService.getManager(task.getProject().getProjectId()).getUserId().equals(managerId)) {
            throw new CodeException("You are not the manager of this project");
        }
        if (managerId.equals(memberId)) {
            throw new CodeException("You cannot remove yourself from the task");
        }
        MemberTask memberTask = task.getMemberTasks().stream()
                .filter(mt -> mt.getMember().getUserId().equals((memberId)))
                .findFirst()
                .orElseThrow(() -> new CodeException("User is not a member of this task"));
        task.getMemberTasks().remove(memberTask);
        taskRepository.save(task);
        memberTaskRepository.delete(memberTask);
        return "Member " + userService.getUserById(memberId).getUsername() + " removed from task " + task.getTitle();
    }

    public Task updateTask(Integer taskId, TaskRequest taskRequest, Integer managerId) throws CodeException {
        Task task = getTaskById(taskId);
        if (projectService.getManager(task.getProject().getProjectId()).getUserId().equals(managerId)) {
            throw new CodeException("You are not the manager of this project");
        }
        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }
        if (taskRequest.getDueDate() != null) {
            task.setDueDate(taskRequest.getDueDate());
        }
        return taskRepository.save(task);
    }

    public void deleteTask(Integer taskId, Integer managerId) throws CodeException {
        Task task = getTaskById(taskId);
        if (!projectService.getManager(task.getProject().getProjectId()).getUserId().equals(managerId)) {
            throw new CodeException("You are not the manager of this project");
        }
        taskRepository.deleteById(taskId);
    }

    public List<User> getMembers(Integer taskId) {
        return memberTaskRepository.getMembersByTaskId(taskId);
    }

    public Boolean isOnTime(Integer taskId, Integer memberId) throws CodeException {
        Task task = getTaskById(taskId);
        User user = userService.getUserById(memberId);
        if (user.getUserId().equals((memberId))) {
            throw new CodeException("User is not a member of this project");
        }
        MemberTask memberTask = memberTaskRepository.findByMemberAndTask(user, task);
        return task.getDueDate().after(new Date()) && memberTask.getIs_completed();
    }

        public Boolean isOverdue(Integer taskId, Integer memberId) throws CodeException {
            Task task = getTaskById(taskId);
            User user = userService.getUserById(memberId);
            if (user.getUserId().equals((memberId))) {
                throw new CodeException("User is not a member of this project");
            }
            MemberTask memberTask = memberTaskRepository.findByMemberAndTask(user, task);
            return task.getDueDate().before(new Date()) && !memberTask.getIs_completed();
        }

    public Task completeTask(Integer taskId, Integer memberId) throws CodeException {
        Task task = getTaskById(taskId);
        User user = userService.getUserById(memberId);
        MemberTask memberTask = memberTaskRepository.findByMemberAndTask(user, task);
        memberTask.setIs_completed(true);
        memberTask.setCompletedDate(new Date());
        memberTaskRepository.save(memberTask);
        return task;
    }

    public Task updateDueDate(Integer taskId, Date dueDate) throws CodeException {
        Task task = getTaskById(taskId);
        task.setDueDate(dueDate);
        return taskRepository.save(task);
    }

}
