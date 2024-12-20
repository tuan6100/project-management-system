package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.GetAllMemberForProjectResponse;
import com.project.oop.PMS.dto.ProjectRequest;
import com.project.oop.PMS.dto.ProjectResponse;
import com.project.oop.PMS.dto.ProjectResponseForProjectDetails;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.service.ProjectService;
import com.project.oop.PMS.service.implement.ProjectServiceImplement;
import com.project.oop.PMS.service.implement.ProjectServiceImplementTA;
import com.project.oop.PMS.service.implement.ProjectServiceImplementTrung;
import com.project.oop.PMS.service.implement.UserServiceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "https://pms.daokiencuong.id.vn")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {


    @Qualifier("projectServiceImplement")
    @Autowired
    private ProjectService projectService = new ProjectServiceImplement();

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
//        boolean isMember = projectService.isMemberOfProject(user, project);
//        if (isMember) {
//            List<GetAllMemberForProjectResponse> memberProjects = projectService.getMembers(projectId);
//            return ResponseEntity.ok(memberProjects);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
        List<GetAllMemberForProjectResponse> memberProjects = projectService.getMembers(userId, projectId);
        if (memberProjects == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(memberProjects);
    }

    @PostMapping("/{projectId}/member/add/{managerId}")
    public ResponseEntity<ProjectResponse> addMemberToProject(@PathVariable Integer projectId,
                                                      @PathVariable Integer managerId,
                                                      @RequestBody List<String> newMembers
                                                      ) throws CodeException {
        Project project = projectServiceTrung.addMember(projectId, managerId, newMembers);
        return ResponseEntity.ok(projectServiceTrung.getProjectResponse(project));
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