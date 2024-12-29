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
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> addProject(@RequestBody ProjectRequest request, @PathVariable Integer managerId) throws CodeException {
        projectService.createProject(request, managerId);
        return ResponseEntity.ok("Project created successfully!");
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
        List<GetAllMemberForProjectResponse> memberProjects = projectService.getMembers(userId, projectId);
        if (memberProjects == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(memberProjects);
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

    @DeleteMapping("/{projectId}/member/remove/{managerId}/{memberName}")
    public ResponseEntity<String> removeMemberFromProject(@PathVariable Integer projectId,
                                                          @PathVariable Integer managerId,
                                                          @PathVariable String memberName) throws CodeException {
        // Tìm memberId dựa trên memberName
        Integer memberId = userService.getUserIdByUsername(memberName);
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Member with name " + memberName + " not found");
        }

        // Gọi hàm removeMember với memberId
        projectService.removeMember(projectId, managerId, memberId);

        // Trả về phản hồi thành công
        return ResponseEntity.ok("Member " + memberName + " removed successfully");
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
            Object reportResult = switch (reportType.toLowerCase()) {
                case "rate-complete-user" -> projectService.rateCompleteTaskByProjectOfUser(projectId);
                case "rate-complete-task" -> projectService.getRateCompleteOfTask(projectId);
                case "overdue-tasks" -> projectService.OverdueTask(projectId);
                default -> throw new IllegalArgumentException("Invalid report type: " + reportType);
            };
            return ResponseEntity.ok(reportResult);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}