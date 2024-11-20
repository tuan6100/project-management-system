package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.Project;

import lombok.Data;

@Data
public class ProjectResponse {
    private Integer id;
    private String name;
    private String description;
    public ProjectResponse(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription()
        );
    }
}
