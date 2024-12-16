package com.project.oop.PMS.dto;

import com.project.oop.PMS.dto.UserResponse;
import com.project.oop.PMS.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseForProjectDetails {

    private Integer id;
    private String name;
    private String description;
    private Date createDate;
    private UserResponse manager;
    private int taskCount;
    private int memberCount;
    private double progress; // Tiến độ dự án
    private int inProgressTaskCount; // Số task đang "In Progress"
    private List<UserResponse> members = new ArrayList<>();

    public static ProjectResponseForProjectDetails fromEntity(Project project, UserResponse manager,
                                             List<UserResponse> members, int totalTasks,
                                             int inProgressTasks, double progress) {
        ProjectResponseForProjectDetails projectResponse = new ProjectResponseForProjectDetails();
        projectResponse.setId(project.getProjectId());
        projectResponse.setName(project.getName());
        projectResponse.setDescription(project.getDescription());
        projectResponse.setCreateDate(project.getCreateDate());
        projectResponse.setManager(manager);
        projectResponse.setMembers(members);
        projectResponse.setTaskCount(totalTasks);
        projectResponse.setMemberCount(members != null ? members.size() : 0);
        projectResponse.setProgress(progress);
        projectResponse.setInProgressTaskCount(inProgressTasks);

        return projectResponse;
    }

}
