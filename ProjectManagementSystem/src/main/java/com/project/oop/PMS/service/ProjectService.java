package com.project.oop.PMS.service;

import com.project.oop.PMS.dto.ProjectRequest;
import com.project.oop.PMS.dto.TaskResponse;
import com.project.oop.PMS.entity.MemberTask;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private TaskService taskService;

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

    public Project getProjectById(Integer projectId) throws CodeException {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CodeException("Project not found"));
    }

    public User getManager(Integer projectId) throws CodeException {
        Project project = getProjectById(projectId);
        return project.getManager();
    }

    public List<User> getMembers(Integer projectId) throws CodeException {
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

    public List<Task> getTasks(Integer projectId) throws CodeException {
        Project project = getProjectById(projectId);
        return project.getTasks();
    }

    public List<TaskResponse> getTasksInProject(Integer projectId) throws CodeException {
        Project project = getProjectById(projectId);
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (Task task : project.getTasks()) {
            taskResponses.add(TaskResponse.fromEntity(task));
        }
        return taskResponses;
    }

    public List<TaskResponse> getTaskCompleted(Integer projectId) throws CodeException {
        Project project = getProjectById(projectId);
        List<Task> tasks = project.getTasks();
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDueDate().after(new Date()) && task.getMemberTasks().stream().allMatch(MemberTask::getIsCompleted)) {
                taskResponses.add(TaskResponse.fromEntity(task));
            }
        }
        return taskResponses;
    }

    public List<TaskResponse> getTaskOverdue(Integer projectId) throws CodeException {
        Project project = getProjectById(projectId);
        List<Task> tasks = project.getTasks();
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDueDate().before(new Date()) && task.getMemberTasks().stream().anyMatch(mt -> !mt.getIsCompleted())) {
                taskResponses.add(TaskResponse.fromEntity(task));
            }
        }
        return taskResponses;
    }

    public Integer getAmountOfTask(Integer projectId) throws CodeException {
        Project project = getProjectById(projectId);
        return project.getTasks().size();
    }

    public Integer getProgress(Integer projectId) throws CodeException {
        Project project = getProjectById(projectId);
        List<Task> tasks = project.getTasks();
        if (tasks.isEmpty()) {
            return 0;
        }
        long totalTasks = tasks.size();
        long completedTasks = getTaskCompleted(projectId).size();
        return (int) (completedTasks * 100 / totalTasks);
    }

}
