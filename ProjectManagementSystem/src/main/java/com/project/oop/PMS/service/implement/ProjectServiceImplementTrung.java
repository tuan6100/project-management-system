package com.project.oop.PMS.service.implement;

import com.project.oop.PMS.dto.*;
import com.project.oop.PMS.entity.*;
import com.project.oop.PMS.exception.CodeException;

import com.project.oop.PMS.repository.*;

import com.project.oop.PMS.repository.MemberProjectRepository;
import com.project.oop.PMS.repository.ProjectRepository;
import com.project.oop.PMS.repository.TaskRepository;

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
    @Autowired
    private MemberTaskRepository memberTaskRepository;
    @Autowired
    private UserRepository userRepository;

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
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CodeException("Project not found"));
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

    @Override
    public List<Integer> getMembersIdOfProject(Integer projectId) {
        return List.of();
    }

    public List<GetAllMemberForProjectResponse> getMembers(Integer userId, Integer projectId) {
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
//    public  List<MemberProject> getAllMembersForProject(Integer projectId) {
//        List<MemberProject> members = memberProjectRepository.findMemberProjectsByProjectId(projectId);
//        return members;
//    }
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

    public boolean isMemberOfTask(Integer taskId, int memberId) {
        List<MemberTask> members = memberTaskRepository.findMemberTaskByTaskId(taskId);
        for(MemberTask memberTask : members) {
            if(memberTask.getMember().getUserId().equals(memberId)) {
                return true;
            }
        }
        return false;
    }

    public void updateCompleteTask(Integer taskId, Integer memberId) throws CodeException {
        // Lấy thông tin thành viên và task từ database
        User member = userRepository.findByUserId(memberId);
        Task task = taskRepository.findByTaskId(taskId);

        // Kiểm tra xem thành viên có thuộc task này không
        if (!isMemberOfTask(taskId, memberId)) {
            throw new CodeException("Member not in this task");
        }

        // Tìm hoặc tạo mới MemberTask
        MemberTask memberTask = memberTaskRepository.findByMemberAndTask(member, task);
        if (memberTask == null) {
            throw new CodeException("MemberTask không tồn tại!");
        }

        // Cập nhật trạng thái hoàn thành cho thành viên
        if (!memberTask.getIs_completed()) {
            memberTask.setIs_completed(true);
            memberTask.setCompletedDate(new Date()); // Ghi thời gian hoàn thành
            memberTaskRepository.saveAndFlush(memberTask); // Lưu và flush
        } else {
            return; // Nếu member đã hoàn thành, không làm gì thêm
        }

        // Kiểm tra còn thành viên nào chưa hoàn thành không
        boolean hasPendingMembers = memberTaskRepository.existsByTaskAndIsCompleted(task, false);
        if (hasPendingMembers) {
            return; // Nếu vẫn còn thành viên chưa hoàn thành, dừng lại
        }

        // Cập nhật trạng thái của task (nếu tất cả thành viên đã hoàn thành)
        Date now = new Date();
        task.setStatus(Task.TaskStatus.completed);
        task.setCompleteDate(now); // Ghi thời gian hoàn thành của task
        task.setIsOverdue(task.getDueDate().before(now)); // Kiểm tra trạng thái overdue
        taskRepository.saveAndFlush(task); // Lưu và flush trạng thái của task
    }


    public List<TaskResponseForGetAllOfMember> getAllTaskOfMember(Integer memberId) throws CodeException {
        List<TaskResponseForGetAllOfMember> taskResponses = new ArrayList<>();

        // Lấy danh sách Task từ MemberTask
        List<Task> listTask = memberTaskRepository.getTasksByUserId(memberId);

        for (Task task : listTask) {
            // Lấy thông tin Project của Task
            Project project = task.getProject();
            if (project == null) {
                throw new CodeException("Project not found for task with ID: " + task.getTaskId());
            }

            // Tìm Manager của Project
            User manager = memberProjectRepository.findManagerIdByProjectId(project.getProjectId());
            if (manager == null) {
                throw new CodeException("Manager not found for project with ID: " + project.getProjectId());
            }

            // Chuyển đổi sang DTO
            TaskResponseForGetAllOfMember taskResponse = TaskResponseForGetAllOfMember.fromEntity(task, project, manager);
            taskResponses.add(taskResponse);
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
