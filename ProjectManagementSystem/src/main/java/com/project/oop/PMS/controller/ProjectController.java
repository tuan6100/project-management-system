package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.GetAllMemberForProjectResponse;
import com.project.oop.PMS.dto.ProjectRequest;
import com.project.oop.PMS.dto.ProjectResponse;
import com.project.oop.PMS.dto.ProjectResponseForProjectDetails;
import com.project.oop.PMS.entity.MemberProject;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.service.implement.ProjectServiceImplement;
import com.project.oop.PMS.service.implement.ProjectServiceImplementTrung;
import com.project.oop.PMS.service.implement.UserServiceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectServiceImplement projectService;

    @Autowired
    private UserServiceImplement userService;

    @Autowired
    private ProjectServiceImplementTrung projectServiceTrung;

    @PostMapping("/add/{managerId}")
    public ResponseEntity<ProjectResponse> addProject(@RequestBody ProjectRequest request, @PathVariable Integer managerId) throws CodeException {
        Project project = projectService.createProject(request, managerId);
        return ResponseEntity.ok(projectService.getProjectResponse(project));
    }

    @GetMapping("/{projectId}/details")
    public ResponseEntity<ProjectResponseForProjectDetails> getProjectInfo(@PathVariable Integer projectId) throws CodeException {
        ProjectResponseForProjectDetails projectDetails = projectServiceTrung.getProjectDetail(projectId);
        return ResponseEntity.ok(projectDetails);
    }

    @GetMapping("/{userId}/{projectId}/members")
    public ResponseEntity<List<GetAllMemberForProjectResponse>> getProjectMembers(@PathVariable Integer projectId, @PathVariable Integer userId) throws CodeException {
        User user = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);
        boolean isMember = projectService.isMemberOfProject(user, project);
        if (isMember) {
            List<GetAllMemberForProjectResponse> memberProjects = projectService.getMembers(projectId);
            return ResponseEntity.ok(memberProjects);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{projectId}/member/add/{managerId}")
    public ResponseEntity<List<GetAllMemberForProjectResponse>> addMemberToProject(
            @PathVariable Integer projectId,
            @PathVariable Integer managerId,
            @RequestBody List<String> newMembers
    ) throws CodeException {
        List<GetAllMemberForProjectResponse> updatedMembers = projectService.addMember(projectId, managerId, newMembers);
        return ResponseEntity.ok(updatedMembers);
    }


    @DeleteMapping("/{projectId}/member/remove/{managerId}/{memberId}")
    public ResponseEntity<String> removeMemberFromProject(@PathVariable Integer projectId,
                                                           @PathVariable Integer managerId,
                                                           @PathVariable Integer memberId
                                                           ) throws CodeException {
        projectService.removeMember(projectId, managerId, memberId);
        return ResponseEntity.ok("Member " +  userService.getUserById(memberId).getUsername() + " removed successfully");
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Integer projectId, @RequestParam Integer userId, @RequestBody ProjectRequest projectRequest) throws CodeException {
        Project project = projectService.updateProject(projectId, userId, projectRequest);
        return ResponseEntity.ok(Map.of(
                "message", "Project updated successfully",
                "project", Map.of(
                        "id", project.getProjectId(),
                        "name", project.getName(),
                        "description", project.getDescription()
                )
        ));
    }

    @DeleteMapping("/{projectId}/delete/{managerId}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer projectId, @PathVariable Integer managerId) throws CodeException {
        projectService.deleteProject(projectId, managerId);
        return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
    }

    @GetMapping("/{projectId}/report/{reportType}")
    public ResponseEntity<?> generateReport(
            @PathVariable Integer projectId,
            @PathVariable String reportType) {
        try {
            Object reportResult;
            switch (reportType.toLowerCase()) {
                case "rate-complete-user":
                    reportResult = projectService.rateCompleteTaskByProjectOfUser(projectId);
                    break;
                case "rate-complete-task":
                    reportResult = projectService.getRateCompleteOfTask(projectId);
                    break;
                case "overdue-tasks":
                    reportResult = projectService.OverdueTask(projectId);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid report type: " + reportType);
            }
            return ResponseEntity.ok(reportResult);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}