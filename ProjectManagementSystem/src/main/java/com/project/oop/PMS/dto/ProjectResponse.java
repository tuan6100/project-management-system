package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.Project;

import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Integer id;
    private String name;
    private String description;
    private UserResponse manager;
    private List<UserResponse> members;
    private List<TaskResponse> tasks;


    public static ProjectResponse fromEntity(Project project) {
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getProjectId());
        projectResponse.setName(project.getName());
        projectResponse.setDescription(project.getDescription());
        if (project.getManager() != null) {
            projectResponse.setManager(UserResponse.fromEntity(project.getManager()));
        } else {
            projectResponse.setManager(null);
        }
        List<UserResponse> userResponses = new ArrayList<>();
        if (project.getMembers() != null) {
            for (User member : project.getMembers()) {
                userResponses.add(UserResponse.fromEntity(member));
            }
        }
        projectResponse.setMembers(userResponses);
        List<TaskResponse> taskResponses = new ArrayList<>();
        if (project.getTasks() != null) {
            for (Task task : project.getTasks()) {
                taskResponses.add(TaskResponse.fromEntity(task));
            }
        }
        projectResponse.setTasks(taskResponses);
        return projectResponse;
    }


}
