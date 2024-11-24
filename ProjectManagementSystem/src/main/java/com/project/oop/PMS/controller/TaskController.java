package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.TaskRequest;
import com.project.oop.PMS.dto.TaskResponse;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.service.ProjectService;
import com.project.oop.PMS.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectService projectService;


    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(@PathVariable Integer projectId) throws CodeException {
        List<Task> tasks = projectService.getProjectById(projectId).getTasks();
        List<TaskResponse>  taskResponses = new ArrayList<>();
        for (Task task : tasks) {
            taskResponses.add(TaskResponse.fromEntity(task));
        }
        return ResponseEntity.ok().body(taskResponses);
    }

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest, @PathVariable Integer projectId, @RequestParam  Integer managerId) throws CodeException {
        return ResponseEntity.ok().body(TaskResponse.fromEntity(taskService.createTask(taskRequest, projectId, managerId)));
    }

    @PostMapping("{taskId}/assign")
    public ResponseEntity<String> assignMember(@PathVariable Integer taskId, @RequestParam  Integer managerId, @RequestParam Integer memberId) throws CodeException {
        return ResponseEntity.ok(taskService.assignMember(taskId, memberId, managerId));
    }

    @GetMapping("{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer taskId) throws CodeException {
        return ResponseEntity.ok().body(TaskResponse.fromEntity(taskService.getTaskById(taskId)));
    }

    @PutMapping("{taskId}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Integer taskId, @RequestParam Integer memberId) throws CodeException {
        return ResponseEntity.ok().body(TaskResponse.fromEntity(taskService.completeTask(taskId, memberId)));
    }

    @PutMapping("{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Integer taskId, @RequestBody TaskRequest taskRequest, @RequestParam Integer managerId) throws CodeException {
        return ResponseEntity.ok().body(TaskResponse.fromEntity(taskService.updateTask(taskId, taskRequest, managerId)));
    }

    @DeleteMapping("{taskId}/remove")
    public ResponseEntity<String> removeMember(@PathVariable Integer taskId, @RequestParam  Integer managerId, @RequestParam Integer memberId) throws CodeException {
        return ResponseEntity.ok(taskService.removeMember(taskId, memberId, managerId));
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer taskId, @RequestParam Integer managerId) throws CodeException {
        taskService.deleteTask(taskId, managerId);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
