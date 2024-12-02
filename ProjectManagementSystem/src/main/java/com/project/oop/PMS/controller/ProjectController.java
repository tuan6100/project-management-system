package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.ProjectRequest;
import com.project.oop.PMS.dto.ProjectResponse;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.service.ProjectService;
import com.project.oop.PMS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @PostMapping("/add/{managerId}")
    public ResponseEntity<ProjectResponse> addProject(@RequestBody ProjectRequest request, @PathVariable Integer managerId) throws CodeException {
        Project project = projectService.createProject(request, managerId);
        return ResponseEntity.ok(projectService.getProjectResponse(project));
    }

    @GetMapping("/{projectId}/info")
    public ResponseEntity<ProjectResponse> getProjectInfo(@PathVariable Integer projectId) throws CodeException {
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(projectService.getProjectResponse(project));
    }

    @PostMapping("{projectId}/member/add/{managerId}")
    public ResponseEntity<ProjectResponse> addMemberToProject(@PathVariable Integer projectId,
                                                      @PathVariable Integer managerId,
                                                      @RequestBody List<Integer> newMembers
                                                      ) throws CodeException {
        Project project = projectService.addMember(projectId, managerId, newMembers);
        return ResponseEntity.ok(projectService.getProjectResponse(project));
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





}