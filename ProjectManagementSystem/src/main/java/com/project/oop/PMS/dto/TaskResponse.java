package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.MemberTask;
import com.project.oop.PMS.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private Integer projectId;
    private Integer taskId;
    private String title;
    private String dueDate;
    private String status;
    private List<MemberTaskResponse> members = new ArrayList<>();

    public  static TaskResponse fromEntity(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setProjectId(task.getProject().getProjectId());
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDueDate(task.getDueDate().toString());
        taskResponse.setStatus(task.getStatus().toString());
        List<MemberTask> memberTasks = task.getMemberTasks();
        for (MemberTask memberTask : memberTasks) {
            taskResponse.getMembers().add(MemberTaskResponse.fromEntity(memberTask));
        }
        return taskResponse;
    }
}
