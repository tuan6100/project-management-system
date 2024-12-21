package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.*;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.UserRepository;
import com.project.oop.PMS.service.implement.ProjectServiceImplementTrung;
import com.project.oop.PMS.service.implement.UserServiceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceImplement userService;
    @Autowired
    private ProjectServiceImplementTrung projectService;

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

}
