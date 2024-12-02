package com.project.oop.PMS.dto;


import com.project.oop.PMS.entity.Project;
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
    private List<UserResponse> members = new ArrayList<>();
    private List<TaskResponse> tasks = new ArrayList<>();

    public static ProjectResponse fromEntity(Project project, UserResponse manager,
                                             List<UserResponse> members, List<TaskResponse> tasks) {
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getProjectId());
        projectResponse.setName(project.getName());
        projectResponse.setDescription(project.getDescription());
        projectResponse.setManager(manager);
        projectResponse.setMembers(members);
        projectResponse.setTasks(tasks);
        return projectResponse;
    }
}


