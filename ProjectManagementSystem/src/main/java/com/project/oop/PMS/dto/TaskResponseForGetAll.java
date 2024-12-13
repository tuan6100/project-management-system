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
public class TaskResponseForGetAll {

    private Integer projectId;
    private Integer taskId;
    private String title;
    private String dueDate;
    private String status;

    public  static TaskResponseForGetAll fromEntity(Task task) {
        TaskResponseForGetAll taskResponse = new TaskResponseForGetAll();
        taskResponse.setProjectId(task.getProject().getProjectId());
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDueDate(task.getDueDate().toString());
        taskResponse.setStatus(task.getStatus().toString());
        return taskResponse;
    }
}
