package com.project.oop.PMS.service.implement;

import com.project.oop.PMS.dto.*;
import com.project.oop.PMS.entity.*;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.MemberProjectRepository;
import com.project.oop.PMS.repository.MemberTaskRepository;
import com.project.oop.PMS.repository.ProjectRepository;
import com.project.oop.PMS.repository.TaskRepository;
import com.project.oop.PMS.repository.UserRepository;
import com.project.oop.PMS.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImplementTA implements ProjectService {


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberProjectRepository memberProjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private UserServiceImplement userService;

    @Autowired
    @Lazy
    private TaskServiceImplement taskService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MemberTaskRepository memberTaskRepository;
    
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

    public Project getProjectById(Integer projectId) throws CodeException {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CodeException("Project not found"));
    }

    public User getManager(Integer projectId) {
        return memberProjectRepository.findManagerIdByProjectId(projectId);
    }

    @Override
    public List<Integer> getMembersIdOfProject(Integer projectId) {
        List<MemberProject> members = memberProjectRepository.findMemberProjectsByProjectId(projectId);
        List<Integer> usersId = new ArrayList<>();
        for(MemberProject member : members) {
            usersId.add(member.getUser().getUserId());
        }
        return usersId;
    }


    public List<GetAllMemberForProjectResponse> getMembers(Integer userId, Integer projectId) {
        if (!getMembersIdOfProject(projectId).contains(userId)) {
            return null;
        }
        List<MemberProject> members = memberProjectRepository.findMemberProjectsByProjectId(projectId);
        List<GetAllMemberForProjectResponse> users = new ArrayList<>();
        for(MemberProject member : members) {
            users.add(GetAllMemberForProjectResponse.fromEntity(member));
        }
        return users;
    }
    public List<GetAllMemberForProjectResponse> getMembers(Integer projectId) {
        List<MemberProject> members = memberProjectRepository.findMemberProjectsByProjectId(projectId);
        List<GetAllMemberForProjectResponse> users = new ArrayList<>();
        for(MemberProject member : members) {
            users.add(GetAllMemberForProjectResponse.fromEntity(member));
        }
        return users;
    }
    public List<User> getMembersNotManager(Integer projectId) {
        return memberProjectRepository.findMemberNotManagerByProjectId(projectId);
    }

    @Override
    public Project addMember(Integer projectId, Integer managerId, List<String> userNames) throws CodeException {
        if (!getManager(projectId).getUserId().equals(managerId)) {
            throw new CodeException("You do not have permission to do");
        }

        List<String> errors = new ArrayList<>();

        // Duyệt qua danh sách tên người dùng
        for (String userName : userNames) {
            try {

                // Lấy đối tượng User từ tên người dùng
                User user = userRepository.findByUserName(userName);

                // Kiểm tra nếu người dùng chưa là thành viên
                if (!getMembers(projectId).contains(user)) {

                    MemberProject memberProject = new MemberProject(user, getProjectById(projectId));
                    memberProjectRepository.save(memberProject);
                } else {
                    errors.add("User " + userName + " is already a member of the project");
                }
            } catch (CodeException e) {
                errors.add("Error with user " + userName + ": " + e.getMessage());
            }
        }

        // Nếu có lỗi thì ném ra exception
        if (!errors.isEmpty()) {
            throw new CodeException(String.join("; ", errors));
        }

        return getProjectById(projectId);
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
    @Override
    public List<RateReport> rateCompleteTaskByProjectOfUser(Integer projectId) throws CodeException {
        List<Task> tasks = taskRepository.findAllByProjectId(projectId);
        if (tasks.isEmpty()) {
            throw new CodeException("No tasks found for the given project.");
        }

        Map<Integer, Integer> totalTasksMap = new HashMap<>();
        Map<Integer, Integer> completedTasksMap = new HashMap<>();
        Map<Integer, String> userNamesMap = new HashMap<>();

        for (Task task : tasks) {
            List<MemberTask> memberTasks = memberTaskRepository.findMemberTaskByTaskId(task.getTaskId());
            for (MemberTask memberTask : memberTasks) {
                User user = memberTask.getMember();
                if (user != null) {
                    Integer userId = user.getUserId();
                    String userName = user.getUsername();
                    userNamesMap.putIfAbsent(userId, userName);
                    totalTasksMap.put(userId, totalTasksMap.getOrDefault(userId, 0) + 1);
                    if (Boolean.TRUE.equals(memberTask.getIs_completed())) {
                        completedTasksMap.put(userId, completedTasksMap.getOrDefault(userId, 0) + 1);
                    }
                }
            }
        }
        List<RateReport> reports = new ArrayList<>();
        for (Integer userId : totalTasksMap.keySet()) {
            int totalTasks = totalTasksMap.get(userId);
            int completedTasks = completedTasksMap.getOrDefault(userId, 0);
            double rate = (totalTasks > 0) ? ((double) completedTasks / totalTasks) * 100 : 0.0;

            reports.add(new RateReport(userId, userNamesMap.get(userId), completedTasks, rate));
        }

        return reports;
    }

    @Override
    public List<RateReport> getRateCompleteOfTask(Integer projectId) throws CodeException {
        List<Task> tasks = taskRepository.findAllByProjectId(projectId);
        if (tasks.isEmpty()) {
            throw new CodeException("No tasks found for the given project.");
        }

        List<RateReport> reports = new ArrayList<>();

        for (Task task : tasks) {
            List<MemberTask> memberTasks = memberTaskRepository.findMemberTaskByTaskId(task.getTaskId());
            int totalMembers = memberTasks.size();
            int completedMembers = 0;
            for (MemberTask memberTask : memberTasks) {
                if (Boolean.TRUE.equals(memberTask.getIs_completed())) {
                    completedMembers++;
                }
            }
            double completionRate = (totalMembers > 0) ? ((double) completedMembers / totalMembers) * 100 : 0.0;
            reports.add(new RateReport(task.getTaskId(), task.getTitle(), completedMembers, completionRate));
        }
        return reports;
    }

    @Override
    public List<TaskResponseForGetAll> OverdueTask(Integer projectId) throws CodeException {
        List<Task> tasks = taskRepository.findAllByProjectId(projectId);
        if (tasks == null || tasks.isEmpty()) {
            throw new CodeException("No tasks found for the given project.");
        }
        List<TaskResponseForGetAll> taskOverdue = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getIsOverdue() != null && task.getIsOverdue()) {
                TaskResponseForGetAll taskResponse = TaskResponseForGetAll.fromEntity(task);
                taskOverdue.add(taskResponse);
            }
        }
        return taskOverdue;
    }
}
