package com.project.oop.PMS.service.implement;

import com.project.oop.PMS.dto.TaskRequest;
import com.project.oop.PMS.entity.*;
import com.project.oop.PMS.entity.Task.TaskStatus;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.MemberProjectRepository;
import com.project.oop.PMS.repository.MemberTaskRepository;
import com.project.oop.PMS.repository.TaskRepository;
import com.project.oop.PMS.repository.UserRepository;
import com.project.oop.PMS.service.NotificationService;
import com.project.oop.PMS.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImplement implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MemberTaskRepository memberTaskRepository;

    @Autowired
    private MemberProjectRepository memberProjectRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private ProjectServiceImplement projectService;

    @Autowired
    @Lazy
    private UserServiceImplement userService;

    @Autowired
    private NotificationService notificationService;


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
        if (!(projectService.getMembersIdOfProject(project.getProjectId()).contains(memberId))) {
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
        
        	task.setStatus(Task.TaskStatus.in_progress);
        
        taskRepository.save(task);
     // Tạo thông báo
        String message = "You have been assigned to the task: " + task.getTitle();
        notificationService.createTaskAssignmentNotification(
            memberId,           // ID của người nhận thông báo
            taskId,             // ID của task
            task.getTitle(),    // Tiêu đề task
            message             // Nội dung thông báo
        );
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
        if (!projectService.getManager(task.getProject().getProjectId()).getUserId().equals(managerId)) {
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
    @Scheduled(cron = "0 * * * * ?") // Chạy mỗi phút

    public void updateOverdueTasks() {
        List<Task> overdueTasks = taskRepository.findAllByDueDateBeforeAndIsOverdueNull(new Date());
        overdueTasks.forEach(task -> task.setIsOverdue(true));
        taskRepository.saveAll(overdueTasks);
    }
    public Map<String, Long> getCompletedTasksInLast7Days(Integer userId) {
        Map<String, Long> result = new LinkedHashMap<>();

        // Ngày hiện tại
        LocalDate today = LocalDate.now();

        // Duyệt từng ngày trong 7 ngày gần nhất
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            Date startDate = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(day.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Gọi phương thức từ MemberTaskRepository để đếm số lượng task hoàn thành của user trong ngày
            Long count = memberTaskRepository.countCompletedTasksByUserAndDateRange(userId, startDate, endDate);

            // Thêm kết quả vào Map
            result.put(day.toString(), count);
        }

        return result;
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
     // Gửi thông báo cho manager về thành viên hoàn thành task
        
        notificationService.notifyManagerTaskCompleted(
              projectService.getManager(getTaskById(taskId).getProject().getProjectId()).getUserId(),
                taskId,
                task.getTitle(),
               userRepository.findByUserId(memberId).getUsername()
        );
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
    public boolean isMemberOfTask(Integer taskId, int memberId) {
        List<MemberTask> members = memberTaskRepository.findMemberTaskByTaskId(taskId);
        for(MemberTask memberTask : members) {
            if(memberTask.getMember().getUserId().equals(memberId)) {
                return true;
            }
        }
        return false;
    }
}
