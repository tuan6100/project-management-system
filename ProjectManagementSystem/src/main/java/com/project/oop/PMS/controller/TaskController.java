package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.TaskRequest;
import com.project.oop.PMS.dto.TaskResponse;
import com.project.oop.PMS.dto.TaskResponseForGetAll;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.service.implement.ProjectServiceImplement;
import com.project.oop.PMS.service.implement.ProjectServiceImplementTrung;
import com.project.oop.PMS.service.implement.TaskServiceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "https://pms.daokiencuong.id.vn")
@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    @Autowired
    private TaskServiceImplement taskService;

    @Autowired
    private ProjectServiceImplement projectService;

    @Autowired
    private ProjectServiceImplementTrung projectServiceTrung;


    @GetMapping
    public ResponseEntity<List<TaskResponseForGetAll>> getAllTasks(@PathVariable Integer projectId) throws CodeException {
        List<Task> tasks = projectService.getProjectById(projectId).getTasks();
        List<TaskResponseForGetAll>  taskResponses = new ArrayList<>();
        for (Task task : tasks) {
            taskResponses.add(TaskResponseForGetAll.fromEntity(task));
        }
        return ResponseEntity.ok().body(taskResponses);
    }

    @PostMapping("/create/{managerId}")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest, @PathVariable Integer projectId, @PathVariable Integer managerId) throws CodeException {
        return ResponseEntity.ok().body(TaskResponse.fromEntity(taskService.createTask(taskRequest, projectId, managerId)));
    }

    @PostMapping("{taskId}/assign/{managerId}/{memberId}")
    public ResponseEntity<String> assignMember(@PathVariable Integer taskId, @PathVariable Integer managerId, @PathVariable Integer memberId) throws CodeException {
        return ResponseEntity.ok(taskService.assignMember(taskId, memberId, managerId));
    }

    @GetMapping("{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer taskId) throws CodeException {
        return ResponseEntity.ok().body(TaskResponse.fromEntity(taskService.getTaskById(taskId)));
    }

//    @PutMapping("{taskId}/complete/{memberId}")
//    public ResponseEntity<TaskResponse> completeTask(@PathVariable Integer taskId, @PathVariable Integer memberId) throws CodeException {
//        return ResponseEntity.ok().body(TaskResponse.fromEntity(taskService.completeTask(taskId, memberId)));
//    }

    @PutMapping("{taskId}/update/{managerId}/{memberId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Integer taskId, @RequestBody TaskRequest taskRequest, @PathVariable Integer managerId) throws CodeException {
        return ResponseEntity.ok().body(TaskResponse.fromEntity(taskService.updateTask(taskId, taskRequest, managerId)));
    }

    @DeleteMapping("{taskId}/remove/{managerId}/{memberId}")
    public ResponseEntity<String> removeMember(@PathVariable Integer taskId, @PathVariable Integer managerId, @PathVariable Integer memberId) throws CodeException {
        return ResponseEntity.ok(taskService.removeMember(taskId, memberId, managerId));
    }

    @DeleteMapping("{taskId}/delete/{managerId}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer taskId, @PathVariable Integer managerId) throws CodeException {
        taskService.deleteTask(taskId, managerId);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @PutMapping("/{taskId}/{memberId}/complete")
    public ResponseEntity<String> updateComplete(
            @PathVariable Integer taskId,
            @PathVariable Integer memberId) {
        try {
            taskService.updateCompleteTask(taskId, memberId);
            return ResponseEntity.ok("Update completed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update task completion: " + e.getMessage());
        }
    }

}
