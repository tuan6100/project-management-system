package com.project.oop.PMS.controller;

import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.service.ProjectService;

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

    // Thêm project
    @PostMapping
    public ResponseEntity<?> addProject(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");

        Project project = projectService.addProject(name, description);
        return ResponseEntity.ok(Map.of(
            "message", "Project added successfully",
            "project", Map.of(
                "id", project.getId(),
                "name", project.getName(),
                "description", project.getDescription()
            )
        ));
    }

    // Xem danh sách project
    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(Map.of("projects", projects));
    }

    // Sửa project
    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");

        Optional<Project> updatedProject = projectService.updateProject(projectId, name, description);
        if (updatedProject.isPresent()) {
            Project project = updatedProject.get();
            return ResponseEntity.ok(Map.of(
                "message", "Project updated successfully",
                "project", Map.of(
                    "id", project.getId(),
                    "name", project.getName(),
                    "description", project.getDescription()
                )
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Project not found"));
        }
    }
    @Transactional
    // Xóa project
    @DeleteMapping("/{projectName}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectName) {
        if (projectService.deleteProjectByName(projectName)) {
            return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Project not found"));
        }
    }
}
