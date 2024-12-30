package com.project.oop.PMS.dto;

import java.util.Date;

import com.project.oop.PMS.entity.MemberProject;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseForGetAllOfMember {
    private Integer projectId;
    private Integer taskId;
    private String title;
    private Integer managerId;
    private String projectName;
    private Date dueDate;
    public static TaskResponseForGetAllOfMember fromEntity(Task task, Project project, User manager) {
        TaskResponseForGetAllOfMember taskResponse = new TaskResponseForGetAllOfMember();
        taskResponse.setProjectId(project.getProjectId());
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setManagerId(manager.getUserId());
        taskResponse.setProjectName(project.getName());
        taskResponse.setDueDate(task.getDueDate());
        return taskResponse;
    }
}
