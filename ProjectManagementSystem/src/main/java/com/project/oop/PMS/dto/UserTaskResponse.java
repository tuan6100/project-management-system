package com.project.oop.PMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskResponse {
    private Integer projectId;
    private Integer taskId;
    private String title;
    private Integer managerId;
    private String projectName;
    private String dueDate;
    private boolean isComplete;
}
