package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.ProjectResponse;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.service.ProjectService;
import com.project.oop.PMS.service.UserService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    // Thêm project
    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        Integer userId = Integer.parseInt(request.get("userId"));

        Optional<User> userOpt = userService.findUserById(userId);
        if (userOpt.isPresent()) {
            Project project = projectService.addProject(name, description, userOpt.get());
            return ResponseEntity.ok(Map.of(
                "message", "Project added successfully",
                "project", Map.of(
                    "id", project.getId(),
                    "name", project.getName(),
                    "description", project.getDescription()
                )
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
    }

    // Xem danh sách project
    @GetMapping("/get/{memberId}")
    public ResponseEntity<List<ProjectResponse>> getAllProjectsByUserId(@PathVariable int memberId) throws Exception {
        List<ProjectResponse> projects = userService.getAllProjectsByUser(memberId);
        return ResponseEntity.ok(projects);
    }
 // Truy cập vào project
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable Integer projectId, @RequestParam Integer userId) {
        Optional<User> userOpt = userService.findUserById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
        User user = userOpt.get();

        Optional<Project> projectOpt = projectService.getProjectById(projectId, user);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            return ResponseEntity.ok(Map.of(
                "id", project.getId(),
                "name", project.getName(),
                "description", project.getDescription(),
                "manager", project.getManager().getUsername(),
                "members", project.getMembers().stream().map(User::getUsername).toArray()
            ));
        } else {
            return ResponseEntity.status(403).body(Map.of("message", "User is not authorized to access this project"));
        }
    }

    @Transactional
    // Xóa project
    @DeleteMapping("/{projectName}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectName, @RequestParam Integer userId) {
        Optional<User> userOpt = userService.findUserById(userId);
        if (userOpt.isPresent()) {
            if (projectService.deleteProjectByName(projectName, userOpt.get())) {
                return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
            } else {
                return ResponseEntity.status(404).body(Map.of("message", "Project not found or user not authorized"));
            }
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
    }
}
