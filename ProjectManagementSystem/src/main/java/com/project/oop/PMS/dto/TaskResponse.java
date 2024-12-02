package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.MemberTask;
import com.project.oop.PMS.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private Integer projectId;
    private Integer taskId;
    private String title;
    private String dueDate;
    private List<MemberTaskResponse> members = new ArrayList<>();

    public  static TaskResponse fromEntity(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setProjectId(task.getProject().getProjectId());
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDueDate(task.getDueDate().toString());
        List<MemberTask> memberTasks = task.getMemberTasks();
        for (MemberTask memberTask : memberTasks) {
            taskResponse.getMembers().add(MemberTaskResponse.fromEntity(memberTask));
        }
        return taskResponse;
    }
}
