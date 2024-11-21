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

    @PostMapping("/add")
    public ResponseEntity<ProjectResponse> addProject(@RequestBody ProjectRequest request) throws CodeException {
        Project project = projectService.createProject(request);
        return ResponseEntity.ok(ProjectResponse.fromEntity(project));
    }

    @GetMapping("/{projectId}/info")
    public ResponseEntity<ProjectResponse> getProjectInfo(@PathVariable Integer projectId) throws CodeException {
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(ProjectResponse.fromEntity(project));
    }

    @PostMapping("{projectId}/member/add")
    public ResponseEntity<ProjectResponse> addMemberToProject(@PathVariable Integer projectId,
                                                      @RequestParam Integer managerId,
                                                      @RequestBody List<Integer> newMembers
                                                      ) throws CodeException {
        Project project = projectService.addMember(projectId, managerId, newMembers);
        return ResponseEntity.ok(ProjectResponse.fromEntity(project));
    }

    @DeleteMapping("/{projectId}/member/remove")
    public ResponseEntity<String> removeMemberFromProject(@PathVariable Integer projectId,
                                                           @RequestParam Integer managerId,
                                                           @RequestParam Integer memberId
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

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer projectId, @RequestParam Integer managerId) throws CodeException {
        projectService.deleteProject(projectId, managerId);
        return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
    }

    @GetMapping("{userId}/get")
    public ResponseEntity<List<ProjectResponse>> getAllProjectsByUserId(@PathVariable int userId) throws CodeException {
        List<ProjectResponse> projects = userService.getAllProjectsByUser(userId);
        return ResponseEntity.ok(projects);
    }


}