package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseForGetAll {
    private Integer id;
    private String name;
    private String description;
    private UserResponse manager;

    public static ProjectResponseForGetAll fromEntity(Project project, UserResponse manager) {
        ProjectResponseForGetAll projectResponse = new ProjectResponseForGetAll();
        projectResponse.setId(project.getProjectId());
        projectResponse.setName(project.getName());
        projectResponse.setDescription(project.getDescription());
        projectResponse.setManager(manager);
        return projectResponse;
    }
}
