package com.project.oop.PMS.service.implement;

import com.project.oop.PMS.dto.*;
import com.project.oop.PMS.entity.*;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.MemberProjectRepository;
import com.project.oop.PMS.repository.MemberTaskRepository;
import com.project.oop.PMS.repository.ProjectRepository;
import com.project.oop.PMS.repository.TaskRepository;
import com.project.oop.PMS.service.ProjectService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImplementTrung implements ProjectService {


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberProjectRepository memberProjectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    @Lazy
    private UserServiceImplement userService;

    @Autowired
    @Lazy
    private TaskServiceImplement taskService;
    @Override
    public Project createProject(ProjectRequest projectRequest, Integer userId) throws CodeException {
        Project project = new Project(projectRequest.getName(), projectRequest.getDescription());
        User manager = userService.getUserById(userId);
        MemberProject memberProject = new MemberProject(manager, project, "Manager");
        memberProjectRepository.save(memberProject);
        return projectRepository.save(project);
    }

    public  List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project updateProject(Integer projectId, Integer managerId, ProjectRequest projectRequest) throws CodeException {
        Project project = getProjectById(projectId);
        if (!getManager(projectId).getUserId().equals(managerId)) {
            throw new CodeException("You do not have permission to do");
        }
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());
        return projectRepository.save(project);
    }

    @Override
    public Project getProjectById(Integer projectId) throws CodeException {
        return null;
    }

    public ProjectResponseForProjectDetails getProjectDetail(Integer projectId) throws CodeException {
        // Lấy thông tin Project từ database
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null) {
            throw new CodeException("Project not found with ID: " + projectId);
        }

        // Lấy thông tin Manager
        User manager = memberProjectRepository.findManagerIdByProjectId(projectId);
        if (manager == null) {
            throw new CodeException("Manager not found for project ID: " + projectId);
        }
        UserResponse managerResponse = new UserResponse(manager.getUserId(), manager.getUsername());

        // Lấy các Members (ngoại trừ Manager)
        List<MemberProject> members = memberProjectRepository.findMemberProjectsByProjectId(projectId);
        List<UserResponse> memberResponses = members.stream()
                .map(member -> new UserResponse(member.getUser().getUserId(), member.getUser().getUsername()))
                .toList();

        // Tính toán số lượng task và tiến độ
        List<Task> tasks = taskRepository.findAllByProjectId(projectId);
        int totalTasks = tasks.size();  // Số lượng task
        long completedTasks = tasks.stream().filter(task -> task.getStatus() == Task.TaskStatus.completed).count();
        int inProgressTasks = (int) tasks.stream().filter(task -> task.getStatus() == Task.TaskStatus.in_progress).count();
        double progress = totalTasks > 0 ? (double) completedTasks / totalTasks : 0.0;

        // Tạo và trả về ProjectResponse mà không có danh sách task
        return ProjectResponseForProjectDetails.fromEntity(
                project,
                managerResponse,
                memberResponses, // Truyền danh sách UserResponse cho thành viên
                totalTasks,         // Số lượng task
                inProgressTasks,    // Số lượng task đang trong tiến trình
                progress            // Tiến độ dự án
        );
    }



    public User getManager(Integer projectId) {
        return memberProjectRepository.findManagerIdByProjectId(projectId);
    }

    public List<GetAllMemberForProjectResponse> getMembers(Integer projectId) {
        List<MemberProject> members = memberProjectRepository.findMemberProjectsByProjectId(projectId);
        List<GetAllMemberForProjectResponse> users = new ArrayList<>();
        for(MemberProject member : members) {
            users.add(GetAllMemberForProjectResponse.fromEntity(member));
        }
        return users;
    }
//    public  List<MemberProject> getAllMembersForProject(Integer projectId) {
//        List<MemberProject> members = memberProjectRepository.findMemberProjectsByProjectId(projectId);
//        return members;
//    }
    public List<User> getMembersNotManager(Integer projectId) {
        return memberProjectRepository.findMemberNotManagerByProjectId(projectId);
    }

    public Project addMember(Integer projectId, Integer managerId, List<Integer> usersId) throws CodeException {
        if (!getManager(projectId).getUserId().equals(managerId)) {
            throw new CodeException("You do not have permission to do");
        }
        List<String> errors = new ArrayList<>();
        usersId.forEach(userId -> {
            User user;
            try {
                user = userService.getUserById(userId);
                if (!getMembers(projectId).contains(user)) {
                    MemberProject memberProject = new MemberProject(user, getProjectById(projectId));
                    memberProjectRepository.save(memberProject);
                } else {
                    assert user != null;
                    errors.add("User " + user.getUsername() + " is already a member of the project");
                }
            } catch (CodeException e) {
                errors.add(e.getMessage());
            }

        });
        if (!errors.isEmpty()) {
            throw new CodeException(String.join("; ", errors));
        }
        return  getProjectById(projectId);
    }

    public void removeMember(Integer projectId, Integer managerId, Integer memberId) throws CodeException {
        if (memberId.equals(managerId)) {
            throw new CodeException("You cannot remove yourself from the project");
        }
        if (!getManager(projectId).getUserId().equals(managerId)) {
            throw new CodeException("You do not have permission to do this");
        }
        Project project = getProjectById(projectId);
        MemberProject memberProject = project.getMembers().stream()
                .filter(mp -> mp.getUser().getUserId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new CodeException("User is not a member of the project"));
        memberProject.getMemberTasks().forEach(memberTask -> {
            try {
                taskService.removeMember(memberTask.getTask().getTaskId(), memberId, managerId);
            } catch (CodeException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        project.getMembers().remove(memberProject);
        projectRepository.save(project);
        memberProjectRepository.delete(memberProject);
    }


    public void deleteProject(Integer projectId, Integer managerId) throws CodeException {
        Project project = getProjectById(projectId);
        if (!getManager(projectId).getUserId().equals(managerId)) {
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
            if (task.getDueDate().after(new Date()) && task.getMemberTasks().stream().allMatch(MemberTask::getIs_completed)) {
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
            if (task.getDueDate().before(new Date()) && task.getMemberTasks().stream().anyMatch(mt -> !mt.getIs_completed())) {
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



    public ProjectResponse getProjectResponse(Project project)  {
        User manager = getManager(project.getProjectId());
        List<MemberProject> members = project.getMembers();
        List<UserResponse> users = new ArrayList<>();
        members.forEach(member -> users.add(UserResponse.fromEntity(member.getUser())));
        List<TaskResponse> tasks = new ArrayList<>();
        project.getTasks().forEach(task -> tasks.add(TaskResponse.fromEntity(task)));
        return ProjectResponse.fromEntity(project, UserResponse.fromEntity(manager), users, tasks);
    }
    public ProjectResponseForGetAll getProjectResponseForGetAll(Project project) {
        User manager = getManager(project.getProjectId());
        return ProjectResponseForGetAll.fromEntity(project, UserResponse.fromEntity(manager));
    }

    public boolean isMemberOfProject(User user, Project project) {
        MemberProject memberProject = memberProjectRepository.findByUserAndProject(user, project);
        return  memberProject != null;
    }
}
