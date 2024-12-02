package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.*;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/{id}")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/get")
    public ResponseEntity<List<ProjectResponse>> getAllProjectsByUserId(@PathVariable int id) throws CodeException {
        List<ProjectResponse> projects = userService.getAllProjects(id);
        return ResponseEntity.ok(projects);
    }


}
