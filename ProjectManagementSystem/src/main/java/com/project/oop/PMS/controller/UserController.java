package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.*;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.UserRepository;
import com.project.oop.PMS.service.implement.ProjectServiceImplementTrung;
import com.project.oop.PMS.service.implement.TaskServiceImplement;
import com.project.oop.PMS.service.implement.UserServiceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "https://pms.daokiencuong.id.vn")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceImplement userService;

    @Autowired
    private ProjectServiceImplementTrung projectService;
    @Autowired
    private TaskServiceImplement taskService;

    @GetMapping("/{id}/get-all-project")
    public ResponseEntity<List<ProjectResponseForGetAll>> getAllProjectsByUserId(@PathVariable int id) throws CodeException {
        List<ProjectResponseForGetAll> projects = userService.getAllProjects(id);
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/{memberId}/tasks")
    public ResponseEntity<List<TaskResponseForGetAllOfMember>> getAllTaskOfMember(@PathVariable Integer memberId) throws CodeException {
        List<TaskResponseForGetAllOfMember> taskResponses = projectService.getAllTaskOfMember(memberId);
        return ResponseEntity.ok(taskResponses);
    }
    @GetMapping("/completed-tasks-last-7-days/{userId}")
    public ResponseEntity<Map<String, Long>> getCompletedTasksLast7Days(@PathVariable Integer userId) {
        Map<String, Long> result = taskService.getCompletedTasksInLast7Days(userId);
        return ResponseEntity.ok(result);
    }
}
