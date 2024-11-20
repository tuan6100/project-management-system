package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.Project;

import com.project.oop.PMS.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectResponse {
    private Integer id;
    private String name;
    private String description;
    private UserResponse manager;
    private List<UserResponse> members;

    public ProjectResponse(Integer id, String name, String description, UserResponse manager, List<UserResponse> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.members = members;
    }

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
        return projectResponse;
    }


}
